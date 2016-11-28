/*
    Dispenser automático de medicamentos.
    Autor: Rodrigo Sclosa <rodrigogs@ciandt.com>
    Data: 05/07/2016
*/
#include <SPI.h>
#include <ESP8266WiFi.h>
#include "RestClient.h"
#include <SimpleTimer.h>
#include <ArduinoJson.h>
#include <Servo.h>
#include "pitches.h"
#include "play_tone.h"

#define BUZZER D1
#define BOTAO D2
#define SERVO_LOCK D7
#define SERVO_TRANSP D8

const String codigoDispenser = "codigo";
const char* ssid     = "";
const char* password = "";
const char* baseurl = "";
const int porta = 443;
const int tempoTimer = 60 * 1000;

unsigned long previousMillis = 0;
const long alert_interval = 15000;           // 15 segundos
int alert_times = 0;
int max_alert_times = 2; //Já tocou a primeira vez, alerta mais 2 vezes

int waitingButtonPress = 0;

SimpleTimer timer;
DynamicJsonBuffer jsonBuffer;

struct Agenda {
  String id;
  int numeroMedicamento;
  int dosagem;
};

//Array de parse dos dados da agenda
struct Agenda agendas[4] = {
                            { "", 0, 0 },
                            { "", 0, 0 },
                            { "", 0, 0 },
                            { "", 0, 0 }
                            };
int totalAgenda = 0;
String response = "";
int buttonState = 0;

Servo lockServo;
Servo transportServo;

int numberOfTrays = 4;
int holeAngle = 170 / (numberOfTrays + 1);
int lockAngleOffset = 7;
int transportAngleOffset = 4;
int servoDelay = 500;
int lastLockPosition = 0;
int lastTransportPosition = 0;

//int melody[] = { NOTE_C4, NOTE_G3,NOTE_G3, NOTE_A3, NOTE_G3, 0, NOTE_B3, NOTE_C4 };
//int noteDurations[] = { 4, 8, 8, 4, 4, 4, 4, 4 };
//
//int melody2[] = { NOTE_C4, NOTE_B3, 0, NOTE_G3, NOTE_A3, NOTE_G3, NOTE_G3, NOTE_C4 };
//int noteDurations2[] = { 8, 8, 8, 4, 4, 4, 8, 8 };
//
//int melody_win[] = { NOTE_A5, NOTE_B5, NOTE_C5, NOTE_B5, NOTE_C5, NOTE_D5, NOTE_C5, NOTE_D5, NOTE_E5, NOTE_D5, NOTE_E5, NOTE_E5, 0 };
//int noteDurations_win[] = { 8, 8, 8, 8, 4, 4, 4, 8, 4, 4, 8, 8, 8 };
//
int melody_loser[] = { NOTE_G4, NOTE_G4, NOTE_C4, NOTE_C4, 0 };
int noteDurations_loser[] = { 8, 8, 8, 8, 8 };

#define N_NOTES_IMP 9
struct song imperial_march[] = {
                                {NOTE_A4,  500},
                                {NOTE_A4,  500},
                                {NOTE_A4,  500},
                                {NOTE_F4,  350},
                                {NOTE_C5,  150},
                                {NOTE_A4,  500},
                                {NOTE_F4,  350},
                                {NOTE_C5,  150},
                                {NOTE_A4, 1000} // 9 notas
                              };

#define N_NOTES_R2 16
struct song r2d2[] = {
                                {NOTE_A7,100},
                                {NOTE_G7,100},
                                {NOTE_E7,100},
                                {NOTE_C7,100},
                                {NOTE_D7,100},
                                {NOTE_B7,100},
                                {NOTE_F7,100},
                                {NOTE_C8,100},
                                {NOTE_A7,100},
                                {NOTE_G7,100},
                                {NOTE_E7,100},
                                {NOTE_C7,100},
                                {NOTE_D7,100},
                                {NOTE_B7,100},
                                {NOTE_F7,100},
                                {NOTE_C8,100}// 16 notas
                              };

RestClient client = RestClient(baseurl, porta);

void VerificaMedicamento() {
  Serial.println("Verificando agenda...");
  VerificaAgenda();
}

void lockTo(int pos, bool skipDelay=false) {
  Serial.println("Lock: ");
  Serial.println(170 - (pos * (holeAngle - 2) + lockAngleOffset));
  
  lockServo.write(170 - (pos * (holeAngle - 2) + lockAngleOffset));
  if (!skipDelay) delay(servoDelay * abs(lastLockPosition - pos));
  lastLockPosition = pos;
}

void transportTo(int pos, bool skipDelay=false) {
  Serial.println("Transport: ");
  Serial.println(pos * (holeAngle - 1) + transportAngleOffset);
  
  transportServo.write(pos * (holeAngle - 1) + transportAngleOffset);
  if (!skipDelay) delay(servoDelay * abs(lastTransportPosition - pos));
  lastTransportPosition = pos;
}

//void beepCommand(String command="") {
//  for (int i=0; i<8; i++) {
//    int noteDuration = 500/noteDurations[i];
//
//    if (melody[i] == 0) {
//      noTone(BUZZER);
//    } else {
//      tone(BUZZER, melody[i]);
//    }
//
//    delay(noteDuration);
//
//    noTone(BUZZER);
//    int pauseBetweenNotes = noteDuration * 1.30;
//    delay(pauseBetweenNotes);
//  }
//}

//void beepCommandAlert(String command="") {
//  for (int i=0; i<8; i++) {
//    int noteDuration2 = 500/noteDurations2[i];
//
//    if (melody2[i] == 0) {
//      noTone(BUZZER);
//    } else {
//      tone(BUZZER, melody2[i]);
//    }
//
//    delay(noteDuration2);
//
//    noTone(BUZZER);
//    int pauseBetweenNotes = noteDuration2 * 1.30;
//    delay(pauseBetweenNotes);
//  }
//}

//void PlayWin() {
//  for (int i=0; i<12; i++) {
//    int noteDuration_win = 500/noteDurations_win[i];
//
//    if (melody_win[i] == 0) {
//      noTone(BUZZER);
//    } else {
//      tone(BUZZER, melody_win[i]);
//    }
//
//    delay(noteDuration_win);
//
//    noTone(BUZZER);
//    int pauseBetweenNotes = noteDuration_win * 1.30;
//    delay(pauseBetweenNotes);
//  }
//}
//
void PlayLoser() {
  for (int i=0; i<4; i++) {
    int noteDuration_loser = 500/noteDurations_loser[i];

    if (melody_loser[i] == 0) {
      noTone(BUZZER);
    } else {
      tone(BUZZER, melody_loser[i]);
    }

    delay(noteDuration_loser);

    noTone(BUZZER);
    int pauseBetweenNotes = noteDuration_loser * 1.30;
    delay(pauseBetweenNotes);
  }
}

void PlayImperialMarch() {
  playTone(BUZZER, imperial_march, N_NOTES_IMP);
}

void PlayR2D2() {
  playTone(BUZZER, r2d2, N_NOTES_R2);
}

void setup() 
{
  Serial.begin(115200);

  pinMode(BUZZER, OUTPUT);
  digitalWrite(BUZZER, LOW);
  
  pinMode(BOTAO, INPUT_PULLUP);
  pinMode(SERVO_LOCK, OUTPUT);
  pinMode(SERVO_TRANSP, OUTPUT);

  lockServo.attach(SERVO_LOCK);
  transportServo.attach(SERVO_TRANSP);

  lockTo(0);
  transportTo(0);

  client.begin(ssid, password);
  client.setContentType("application/json");
  client.setSecureConnection(true);

  timer.setInterval(tempoTimer, VerificaMedicamento); //Timer de 1 minuto
  ResetaTodasDatas(); //Limpa todas as datas dos medicamentos para atual
}

void loop()
{
  //Botão pressionado
  buttonState = digitalRead(BOTAO);

  if (buttonState == HIGH && waitingButtonPress == true) {
    //Repete a notificação a cada 10 segundos até ser pressionado o botão
    unsigned long currentMillis = millis();

    if (currentMillis - previousMillis >= alert_interval) {
      previousMillis = currentMillis;

      if(alert_times == max_alert_times) {
        Serial.print("Excedeu numero de avisos...");
        alert_times = 0;
        previousMillis = millis();
        waitingButtonPress = false;
        PlayLoser();

        //Pra cada item na agenda, envia mensagem de não tomou medicamento
        for (int i=0; i<=totalAgenda; i++) {
          EnviaAvisoNaoTomouMedicamento(agendas[i].id);
        }

        totalAgenda = 0;
      } else {
        //Toca novamente a música para alertar
        Serial.print("Aviso ");
        Serial.println((alert_times+1));
        PlayImperialMarch();
        alert_times++;
      }
    }
  } else if (buttonState == LOW && waitingButtonPress == true) {
    Serial.println("Botão pressionado!");
    alert_times = 0;
    previousMillis = millis();
    
    //Send button pressed
    if(AtulalizaDosagem() == 200) {
      Serial.println("Dosagem atualizada!");
      PlayR2D2();
    
      for (int i=0; i<=totalAgenda; i++) {
        lockTo(agendas[i].numeroMedicamento);
        transportTo(agendas[i].numeroMedicamento);
        transportTo(0);

        if (agendas[i].dosagem > 1) {
          //lockTo(agendas[i].numeroMedicamento);
          transportTo(agendas[i].numeroMedicamento);
          transportTo(0);
        }
      }

      lockTo(0);
      totalAgenda = 0;
      waitingButtonPress = false;
    } else {
      Serial.println("Erro ao atualizar a dosagem!");
      PlayLoser();
    }
  }

  timer.run();
}

int VerificaAgenda() {
  LimpaAgendas();
  Serial.println("Get data: ");

  String url = "/_ah/api/agenda/v1/agora/" + codigoDispenser;

  int str_len = url.length() + 1;
  char char_url[str_len];
  url.toCharArray(char_url, str_len);

  response = "";
  int statusCode = client.get(char_url, &response);

  Serial.print("Status code from server: ");
  Serial.println(statusCode);
  Serial.print("Response body from server: ");
  Serial.println(response);

  if(response != "") {
    JsonObject& root = jsonBuffer.parseObject(response);
    root.prettyPrintTo(Serial);
  
    if (!root.success()) {
      Serial.println("JSON parsing failed!");
      PlayLoser();
      return 0;
    }
    
    JsonArray& items = root["items"].asArray();

    Serial.println("Items Array: ");
    items.prettyPrintTo(Serial);

    totalAgenda = items.size();

    if(totalAgenda > 0) {
      Serial.println("Items: ");
      Serial.println(totalAgenda);
  
      for(int i=0; i <= totalAgenda; i++) {
        Agenda agenda;
        agenda.id = items[i]["id"].as<String>();
        agenda.numeroMedicamento = items[i]["numeroMedicamento"];
        agenda.dosagem = items[i]["dosagem"].as<int>();
        agendas[i] = agenda;
      }

      Serial.print("PlayImperialMarch");
      PlayImperialMarch();
      waitingButtonPress = true;
      previousMillis = millis();
      return 200;
    } else {
      //waitingButtonPress = false;
      return 0;
    }
  }

  return 0;
}

//Método que envia os dados para a API
int AtulalizaDosagem() {
  String idAgenda = "";
  int numeroMedicamento = 0;
  int cont = 0;
  int items = 0;

  for (int i=0; i<=totalAgenda; i++) {
    //items++;

    idAgenda = agendas[i].id;
    numeroMedicamento = agendas[i].numeroMedicamento;

    if (idAgenda != "" && numeroMedicamento > 0) {
      items++;
      
      Serial.println("Id Agenda:");
      Serial.println(idAgenda);
      Serial.println("Numero Medicamento:");
      Serial.println(numeroMedicamento);

      String url = "/_ah/api/agenda/v1/dosagem?id=" + idAgenda;
    
      int str_len = url.length() + 1;
      char char_url[str_len];
      url.toCharArray(char_url, str_len);
    
      String response = "";
      int statusCode = client.post(char_url, "{ OK }", &response);
    
      Serial.print("Status code from server: ");
      Serial.println(statusCode);
      Serial.print("Response body from server: ");
      Serial.println(response);
  
      if(statusCode == 200) {
        cont++;
      }
    }
  }

  Serial.println("Items achados: ");
  Serial.println(items);

  if(items == cont) {
    return 200;
  } else {
    return 404;
  }
}

int ResetaTodasDatas() {
  String url = "/_ah/api/agenda/v1/resettodasdatas/" + codigoDispenser;

  int str_len = url.length() + 1;
  char char_url[str_len];
  url.toCharArray(char_url, str_len);

  String response = "";
  int statusCode = client.post(char_url, "{ OK }", &response);

  Serial.print("Status code from server: ");
  Serial.println(statusCode);
  Serial.print("Response body from server: ");
  Serial.println(response);

  return statusCode;
}

int EnviaAvisoNaoTomouMedicamento(String idAgenda) {
  String url = "/_ah/api/agenda/v1/naotomoumedicamento/" + idAgenda;

  int str_len = url.length() + 1;
  char char_url[str_len];
  url.toCharArray(char_url, str_len);

  String response = "";
  int statusCode = client.post(char_url, "{ OK }", &response);

  Serial.print("Status code from server: ");
  Serial.println(statusCode);
  Serial.print("Response body from server: ");
  Serial.println(response);

  return statusCode;
}

void LimpaAgendas() {
  struct Agenda agendas[4] = {
                { "", 0, 0 },
                { "", 0, 0 },
                { "", 0, 0 },
                { "", 0, 0 }
              };
}


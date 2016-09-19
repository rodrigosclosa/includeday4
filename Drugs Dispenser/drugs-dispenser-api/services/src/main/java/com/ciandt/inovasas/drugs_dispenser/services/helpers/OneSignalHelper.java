package com.ciandt.inovasas.drugs_dispenser.services.helpers;

import com.ciandt.inovasas.drugs_dispenser.services.constants.Params;
import com.ciandt.inovasas.drugs_dispenser.services.models.Agenda;
import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
import com.google.api.server.spi.response.ConflictException;
import com.google.appengine.repackaged.com.google.protobuf.Int32Value;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rodrigosclosa on 19/06/16.
 */

public class OneSignalHelper {

    private static final Logger log = Logger.getLogger(OneSignalHelper.class.getName());

    private RestClient restClient;

    public OneSignalHelper() {
        restClient = new RestClient();
    }

    public String SendPush(String customPushData, String mensagem, String tag) throws ConflictException {
        String retorno = "";

//        log.log(Level.WARNING, "SendPush;" +customPushData + ";"+ mensagem + ";" + carteirinha);

        StringBuilder pushData = new StringBuilder();

        pushData.append("{\n" +
                "  \"app_id\":"+ Params.getInstance().getOneSignalAppId() + ",\n" +
                "  \"included_segments\": [\"All\"],\n" +
                "  \"data\": " + customPushData + ",\n" +
                "  \"contents\": {\"en\": \"" + mensagem + "\"},\n" +
                "  \"tags\": [" + tag + "]\n" +
                "}");

        log.log(Level.WARNING, "SendPush;pushData:" + pushData.toString());

        String pushReturn = restClient.Post("https://onesignal.com/api/v1/notifications",
                pushData.toString(),
                "application/json",
                "N2RkNWQxNTctYmJjYS00OTZkLTgzOTktMTY1YWZlNzU1MmQz");

        log.log(Level.WARNING, "SendPush;retorno:" + pushReturn);

        if(pushReturn != null && !pushReturn.isEmpty()) {
            try {
                JSONObject jsonObj = new JSONObject(pushReturn);

                if(jsonObj != null) {
                    if(jsonObj.getString("id") != null && jsonObj.getInt("recipients") > 0 ) {
                        retorno = "OK";
                    } else {
                        throw new ConflictException("Ocorreu um erro ao enviar o Push.");
                    }
                }
            } catch (JSONException e) {
                throw new ConflictException("Erro no retorno JSON: " + e.getMessage());
            }

        } else {
            throw new ConflictException("Push não pode ser enviado. Verifique os parâmetros de envio.");
        }

        return retorno;
    }

    public String createMsgDrugTime(List<Agenda> agendas, Dispenser dispenser, String numeroCarteirinha) throws ConflictException {
        String medicamento = "";

        Integer cont = 1;
        for (Agenda agenda : agendas) {
            if(cont > 1) {
                if (agendas.size() > 2) {
                    medicamento += ", ";
                } else {
                    medicamento += " e ";
                }
            }
            medicamento += getDrugName(dispenser, agenda.getNumeroMedicamento());
            cont++;
        }

        String mensagem = "";

        if(agendas.size() >= 2) {
            mensagem = "Está na hora dos medicamentos " + medicamento + " no dispenser " + dispenser.getNomeDispenser();

        } else {
            mensagem = "Está na hora do medicamento " + medicamento + " no dispenser " + dispenser.getNomeDispenser();
        }

        String customPushData = "{\"tipo\":\"DispenserList\",\"remedio\":\"" + medicamento +"\"}";
        String tag = Params.getInstance().getOneSignalTag(numeroCarteirinha);

        return SendPush(customPushData, mensagem, tag);
    }

    public String createMsgInfo(String medicamento, String numeroCarteirinha) throws ConflictException {
        String customPushData = "{\"tipo\":\"DispenserList\"}";
        String mensagem = String.format("%s: Paciente medicado.", medicamento);
        String tag = Params.getInstance().getOneSignalTag(numeroCarteirinha);

        return SendPush(customPushData, mensagem, tag);
    }

    public String enviaMensagemNaoTomouMedicamento(String medicamento, String numeroCarteirinha) throws ConflictException {
        String customPushData = "{\"tipo\":\"DispenserList\"}";
        String mensagem = String.format("%s: Paciente não tomou o medicamento no horário.", medicamento);
        String tag = Params.getInstance().getOneSignalTag(numeroCarteirinha);

        return SendPush(customPushData, mensagem, tag);
    }

    private String getDrugName(Dispenser dispenser, Integer codRemedio) {

        if(dispenser.getCodigoMedicamento1() == codRemedio) {
            return dispenser.getDescricaoMedicamento1();
        } else if(dispenser.getCodigoMedicamento2() == codRemedio) {
            return dispenser.getDescricaoMedicamento2();
        } else if(dispenser.getCodigoMedicamento3() == codRemedio) {
            return dispenser.getDescricaoMedicamento3();
        } else if(dispenser.getCodigoMedicamento4() == codRemedio) {
            return dispenser.getDescricaoMedicamento4();
        }

        return "";
    }

}

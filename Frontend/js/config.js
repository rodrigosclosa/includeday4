/// configuração das URLS de APIS
var API_URL = "https://drugs-dispenser-includeday.appspot.com/_ah/api";
var API_CHAT_URL = "http://chat-site.azurewebsites.net/signalr";
var API_SIRENE_URL = "http://api.iot.ciandt.com/v2/data";

/// ids de sirenes
var ID_DISPENSER_BH = '5644406560391168'; 
var ID_DISPENSER_CAMPINAS = '5137355874762752';

/// chave da API do Google Maps
var API_KEY = "AIzaSyCOfscKjGzCub_QJrbXTV8PWS2TP9ayPs4";

/// configuração dos links do menu (tanto lateral como no topo)
var MENU = {
    links:[
    {
        nome: "Agenda",
        url: "agenda.html",
        accesskey: "2"
    },
    {
        nome: "Dispenser",
        url: "dispenser.html",
        accesskey: "3"
    },
]};

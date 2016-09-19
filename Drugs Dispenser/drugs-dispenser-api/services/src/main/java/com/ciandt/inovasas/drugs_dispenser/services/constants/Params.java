package com.ciandt.inovasas.drugs_dispenser.services.constants;

/**
 * Created by fprado on 7/7/16.
 */
public class Params {
    private static Params ourInstance = new Params();

    public static Params getInstance() {
        return ourInstance;
    }

    private Params() {
    }

    public String getURLPushwoosh() {
        return "https://cp.pushwoosh.com/json/1.3";
    }

    public String getPushwooshToken() {
        return "ZXdh6Ps4aOim8r9YeMXSa10QO6pqxPQUXNcVHkY1jvkZXXuKQ7AT8Zt6CKeLKHHAC8tlXzQ9l6VsTwagvT9j";
    }

    public String getOneSignalKey() {
        return "N2RkNWQxNTctYmJjYS00OTZkLTgzOTktMTY1YWZlNzU1MmQz";
    }

    public String getPushwooshAppId() {
        return "47A7A-56674";
    }

    public String getPushwooshTag(String username) {
        return "[\"username\", \"EQ\", \"" + username + "\"]";
    }

    public String getOneSignalTag(String carteirinha) {
        return "{\"key\": \"username\", \"relation\": \"=\", \"value\": \"" + carteirinha + "\"}";
    }

    public String getOneSignalAppId() {
        return "\"d87de829-2bfc-4248-a864-845fc611ccc8\"";
    }

    public String getMensagemAprovacao() {
        return "Recebemos a solicitação para o procedimento %s através do solicitante %s, estamos analisando e em breve atualizaremos o status. Você pode consultar o status a qualquer momento acessando o aplicativo SulAmérica Saúde, na opção Autorização de Procedimentos.";
    }

    public String getMensagemAviso() {
        return "Seu procedimento %s solicitado por %s foi %s pela SulAmérica. Para mais detalhes, clique para abrir a consulta de procedimentos. Em caso de dúvidas entre em contato conosco pelo Chat.";
    }

    public String getMensagemRetornoSolicitacao() {
        return "Seu procedimento %s solicitado foi %s pela SulAmérica. Para mais detalhes, clique para abrir a consulta de procedimentos. Em caso de dúvidas entre em contato conosco pelo Chat.";
    }

    public String getIOSCategoryId() {
        return "2282"; //Action ID do aplictivo iOS (botões que aparecem no push)
    }
}

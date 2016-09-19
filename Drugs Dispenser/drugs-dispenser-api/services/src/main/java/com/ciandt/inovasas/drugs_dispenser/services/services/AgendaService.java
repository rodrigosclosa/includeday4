package com.ciandt.inovasas.drugs_dispenser.services.services;

import com.ciandt.inovasas.drugs_dispenser.services.daos.AgendaDao;
import com.ciandt.inovasas.drugs_dispenser.services.daos.DispenserDao;
import com.ciandt.inovasas.drugs_dispenser.services.helpers.OneSignalHelper;
import com.ciandt.inovasas.drugs_dispenser.services.models.Agenda;
import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
import com.ciandt.inovasas.drugs_dispenser.services.models.Medicamento;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ser.std.StdArraySerializers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rodrigosclosa on 30/06/16.
 */

public class AgendaService {

    private static final Logger log = Logger.getLogger(AgendaService.class.getName());

    private AgendaDao agendaDao;
    private DispenserDao dispenserDao;
    private OneSignalHelper oneSignalHelper;

    public AgendaService() {
        agendaDao = new AgendaDao();
        dispenserDao = new DispenserDao();
        oneSignalHelper = new OneSignalHelper();
    }

    public List<Agenda> list() throws NotFoundException {
        List<Agenda> list = agendaDao.listAll();

        for (Agenda ag : list) {
            Dispenser dispenser = dispenserDao.getByKey(ag.getIdDispenser());

            if(dispenser == null) {
                throw new NotFoundException(String.format("Dispenser não localizado para o código: %s", ag.getIdDispenser()));
            }

            Medicamento medicamento = new Medicamento();
            medicamento.setCodigoMedicamento(ag.getNumeroMedicamento());
            medicamento.setDescricaoMedicamento(getDrugName(dispenser, ag.getNumeroMedicamento()));

            ag.setMedicamento(medicamento);
        }

        return list;
    }

    public List<Agenda> list(String codigoDispenser) throws NotFoundException {
        Dispenser dispenser = dispenserDao.getByProperty("codigo", codigoDispenser);

        if(dispenser == null) {
            throw new NotFoundException(String.format("Dispenser não localizado para o ID: %s", codigoDispenser));
        }

        List<Agenda> list = agendaDao.listByProperty("idDispenser", dispenser.getId());

        if(list == null || list.size() < 1) {
            throw new NotFoundException(String.format("Agenda não localizada para o dispenser código: %s", codigoDispenser));
        }

        for (Agenda ag : list) {
            Medicamento medicamento = new Medicamento();
            medicamento.setCodigoMedicamento(ag.getNumeroMedicamento());
            medicamento.setDescricaoMedicamento(getDrugName(dispenser, ag.getNumeroMedicamento()));

            ag.setMedicamento(medicamento);
        }

        return list;
    }

    public Agenda getById(Long id) throws NotFoundException {
        Agenda item = agendaDao.getByKey(id);

        if(item == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        Dispenser dispenser = dispenserDao.getByKey(item.getIdDispenser());

        if(dispenser == null) {
            throw new NotFoundException(String.format("Dispenser não localizado para o ID: %s", item.getIdDispenser()));
        }

        Medicamento medicamento = new Medicamento();
        medicamento.setCodigoMedicamento(item.getNumeroMedicamento());
        medicamento.setDescricaoMedicamento(getDrugName(dispenser, item.getNumeroMedicamento()));

        item.setMedicamento(medicamento);

        return item;
    }

    public Agenda getByDispenserAndMedicamento(String codigoDispenser, Integer codigoMedicamento) throws NotFoundException {
        Dispenser dispenser = dispenserDao.getByProperty("codigo", codigoDispenser);

        if(dispenser == null) {
            throw new NotFoundException(String.format("Dispenser não localizado para o código: %s", codigoDispenser));
        }

        Query.Filter f1 = new Query.FilterPredicate("idDispenser", Query.FilterOperator.EQUAL, dispenser.getId());
        Query.Filter f2 = new Query.FilterPredicate("numeroMedicamento", Query.FilterOperator.EQUAL, codigoMedicamento);
        Query.Filter filter = Query.CompositeFilterOperator.and(f1, f2);

        Agenda item = agendaDao.getByFilter(filter);

        if(item == null) {
            throw new NotFoundException("Agenda não localizada para o dispenser e medicamento informado!");
        }

        Medicamento medicamento = new Medicamento();
        medicamento.setCodigoMedicamento(codigoMedicamento);
        medicamento.setDescricaoMedicamento(getDrugName(dispenser, codigoMedicamento));

        item.setMedicamento(medicamento);

        return item;
    }

    public List<Agenda> getMedicamentoAgora(String codigoDispenser) throws NotFoundException, ConflictException {
        List<Agenda> retorno = new ArrayList<Agenda>();
        Dispenser dispenser = dispenserDao.getByProperty("codigo", codigoDispenser);

        if(dispenser == null) {
            throw new NotFoundException(String.format("Dispenser não localizado para o código: %s", codigoDispenser));
        }

        Date horaAtual = new Date();

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(horaAtual);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, -3);

        log.log(Level.WARNING, "getMedicamentoAgora: Data agora: " + calendar.getTime());

        //Date horaAtual = new Date(new Date().getTime() - 3 * (3600 * 1000));
        List<Agenda> listaAgenda = agendaDao.listByProperty("idDispenser", dispenser.getId());

        if(listaAgenda != null) {
            for (Agenda agenda : listaAgenda) {
                Date dataNova;
                //Date dataAfter;
                //Date dataBefore;

                if(agenda.getUltimaDosagem() == null) {
                    dataNova = new Date(agenda.getDataInicio().getTime() + (agenda.getIntervaloMinutos() * (60 * 1000)));
                } else {
                    dataNova = new Date(agenda.getUltimaDosagem().getTime() + (agenda.getIntervaloMinutos() * (60 * 1000)));
                }

                log.log(Level.WARNING, "getMedicamentoAgora: Data nova: " + dataNova);

//                dataAfter = new Date(dataNova.getTime() + (60 * 1000));
//                dataBefore = new Date(dataNova.getTime() - (60 * 1000));
//
//                if((horaAtual.after(dataAfter) && horaAtual.before(dataBefore))){ //&& !agenda.isDosagemCaiu()) {
//                    oneSignalHelper.createMsgDrugTime(getDrugName(dispenser, agenda.getNumeroMedicamento()), dispenser.getCarteirinhaAssociado(), dispenser.getNomeDispenser());
//                    return agenda;
//                }

                log.log(Level.WARNING, "getMedicamentoAgora: é igual: " + dataNova.equals(calendar.getTime()));

                if(dataNova.equals(calendar.getTime())) {
                    //send push
                    retorno.add(agenda);
                }
            }
        }

//        if(!retorno.isEmpty()) {
//            oneSignalHelper.createMsgDrugTime(retorno, dispenser, dispenser.getCarteirinhaAssociado());
//        }

        return retorno;
    }

    public Agenda insert(Agenda item) throws ConflictException, NotFoundException {
        if(item == null)
        {
            throw new ConflictException("Dados da agenda não informada.");
        }
        else if(item.getIdDispenser() == null)
        {
            throw new ConflictException("Código do dispenser não informado.");
        }
        else if(item.getIntervaloMinutos() == null) {
            throw new ConflictException("Intervalo de dosagem não informada.");
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(new Date());

        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 10;
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE, mod < 5 ? -mod : (10 - mod));
        calendar.add(Calendar.HOUR, -3);

        item.setDataInicio(calendar.getTime());

        agendaDao.insert(item);

        return item;
    }

    public Agenda update(Agenda item) throws ConflictException, NotFoundException {

        if(item == null)
        {
            throw new ConflictException("Dados da agenda não informada.");
        }
        else if(item.getIdDispenser() == null)
        {
            throw new ConflictException("Código do dispenser não informado.");
        }
        else if(item.getIntervaloMinutos() == null) {
            throw new ConflictException("Intervalo de dosagem não informada.");
        }
        else if(item.getDataInicio() == null) {
            throw new ConflictException("Data de início não informada.");
        }

        Agenda u = agendaDao.getById(item.getId());

        if(u == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(item.getDataInicio());

        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 10;
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE, mod < 5 ? -mod : (10 - mod));
        //calendar.add(Calendar.HOUR, -3);

        item.setDataInicio(calendar.getTime());

        agendaDao.update(item);

        return item;
    }

    public void remove(long id) throws ConflictException, NotFoundException {
        Agenda item = agendaDao.getByKey(id);

        if(item == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        agendaDao.delete(item);
    }

    public Agenda updateUtimaDosagem(Long id) throws NotFoundException, ConflictException {
        Agenda agenda = agendaDao.getById(id);

        if(agenda == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(new Date());

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, -3);

        agenda.setUltimaDosagem(calendar.getTime());
        agenda.setDosagemCaiu(true);

        agendaDao.update(agenda);

        Dispenser dispenser = dispenserDao.getById(agenda.getIdDispenser());

        //send push
        //oneSignalHelper.createMsgInfo(getDrugName(dispenser, agenda.getNumeroMedicamento()), dispenser.getCarteirinhaAssociado());

        return agenda;
    }

    public Agenda resetDatas(Long idAgenda) throws NotFoundException, ConflictException {
        Agenda agenda = agendaDao.getById(idAgenda);

        if(agenda == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(new Date());

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, -3);

        agenda.setDataInicio(calendar.getTime());
        agenda.setUltimaDosagem(null);
        agenda.setDosagemCaiu(false);

        agendaDao.update(agenda);

        return agenda;

    }

    public void resetTodasDatas(String codigoDispenser) throws NotFoundException, ConflictException {
        Dispenser dispenser = dispenserDao.getByProperty("codigo", codigoDispenser);

        if(dispenser == null) {
            throw new NotFoundException(String.format("Dispenser não localizado para o código: %s", codigoDispenser));
        }

        List<Agenda> agendas = agendaDao.listByProperty("idDispenser", dispenser.getId());

        if(agendas == null || agendas.isEmpty()) {
            throw new NotFoundException("Agenda não encontrada para o dispenser.");
        }

        for (Agenda agenda : agendas) {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
            calendar.setTimeZone(tz);
            calendar.setTime(new Date());

            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.HOUR, -3);

            agenda.setDataInicio(calendar.getTime());
            agenda.setUltimaDosagem(null);
            agenda.setDosagemCaiu(false);

            agendaDao.update(agenda);
        }
    }

    public Agenda mandaAvisoNaoTomouMedicamento(Long id) throws NotFoundException, ConflictException {
        Agenda agenda = agendaDao.getById(id);

        if(agenda == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        calendar.setTimeZone(tz);
        calendar.setTime(new Date());

        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.HOUR, -3);

        agenda.setDataInicio(calendar.getTime());
        agenda.setUltimaDosagem(null);
        agenda.setDosagemCaiu(false);

        agendaDao.update(agenda);

        Dispenser dispenser = dispenserDao.getById(agenda.getIdDispenser());

        //send push
        //oneSignalHelper.enviaMensagemNaoTomouMedicamento(getDrugName(dispenser, agenda.getNumeroMedicamento()), dispenser.getCarteirinhaAssociado());

        return agenda;
    }

    public Agenda alterarDosagem(Long idAgenda, Integer dosagem) throws NotFoundException, ConflictException {
        Agenda agenda = agendaDao.getById(idAgenda);

        if(agenda == null) {
            throw new NotFoundException("Agenda não encontrada.");
        }

        agenda.setDosagem(dosagem);

        agendaDao.update(agenda);

        return agenda;
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

package com.ciandt.inovasas.drugs_dispenser.services.endpoints;

import com.ciandt.inovasas.drugs_dispenser.services.models.Agenda;
import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
import com.ciandt.inovasas.drugs_dispenser.services.services.AgendaService;
import com.ciandt.inovasas.drugs_dispenser.services.services.DispenserService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

/**
 * Created by rodrigosclosa on 30/06/16.
 */

@Api(
        name = "agenda",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "services.drugs_dispenser.inovasas.ciandt.com",
                ownerName = "services.drugs_dispenser.inovasas.ciandt.com",
                packagePath = ""
        )
)
public class AgendaEndpoint {

    private AgendaService agendaService;

    public AgendaEndpoint() {
        agendaService = new AgendaService();
    }

    //getAgenda
    //setAgenda
    //insertDispenser
    //deleteDispenser

    @ApiMethod(name = "get", path = "get", httpMethod = "GET")
    public List<Agenda> get(@Nullable @Named("codigoDispenser") String codigoDispenser) throws NotFoundException {
        if (codigoDispenser == null)
            return agendaService.list();
        else
            return agendaService.list(codigoDispenser);
    }

    @ApiMethod(name = "getById", path = "get/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public Agenda getById(@Named("id") Long id) throws NotFoundException {
        return agendaService.getById(id);
    }

    @ApiMethod(name = "getByDispenserAndMedicamento", path = "dispenser/{codigoDispenser}/{codigoMedicamento}", httpMethod = ApiMethod.HttpMethod.GET)
    public Agenda getByDispenserAndMedicamento(@Named("codigoDispenser") String codigoDispenser, @Named("codigoMedicamento") Integer codigoMedicamento) throws NotFoundException {
        return agendaService.getByDispenserAndMedicamento(codigoDispenser, codigoMedicamento);
    }

    @ApiMethod(name = "getMedicamentoAgora", path = "agora/{codigoDispenser}", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Agenda> getMedicamentoAgora(@Named("codigoDispenser") String codigoDispenser) throws NotFoundException, ConflictException {
        return agendaService.getMedicamentoAgora(codigoDispenser);
    }

    @ApiMethod(name = "post", path = "new", httpMethod = ApiMethod.HttpMethod.POST)
    public Agenda post(Agenda item) throws ConflictException, NotFoundException {
        return agendaService.insert(item);
    }

    @ApiMethod(name = "dosagem", path = "dosagem", httpMethod = ApiMethod.HttpMethod.POST)
    public Agenda updateUltimaDosagem(@Named("id") Long id) throws ConflictException, NotFoundException {
        return agendaService.updateUtimaDosagem(id);
    }

    @ApiMethod(name = "put", path = "update", httpMethod = ApiMethod.HttpMethod.PUT)
    public Agenda put(Agenda item) throws NotFoundException, ConflictException {
        return agendaService.update(item);
    }

    @ApiMethod(name = "delete", path = "delete/{id}", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void delete(@Named("id") Long id) throws NotFoundException, ConflictException {
        agendaService.remove(id);
    }

    @ApiMethod(name = "resetDatas", path = "resetdatas/{idAgenda}", httpMethod = ApiMethod.HttpMethod.POST)
    public Agenda resetDatas(@Named("idAgenda") Long idAgenda) throws ConflictException, NotFoundException {
        return agendaService.resetDatas(idAgenda);
    }

    @ApiMethod(name = "resetTodasDatas", path = "resettodasdatas/{codigoDispenser}", httpMethod = ApiMethod.HttpMethod.POST)
    public void resetTodasDatas(@Named("codigoDispenser") String codigoDispenser) throws ConflictException, NotFoundException {
        agendaService.resetTodasDatas(codigoDispenser);
    }

    @ApiMethod(name = "enviaMensagemNaoTomouMedicamento", path = "naotomoumedicamento/{idAgenda}", httpMethod = ApiMethod.HttpMethod.POST)
    public void enviaMensagemNaoTomouMedicamento(@Named("idAgenda") Long idAgenda) throws ConflictException, NotFoundException {
        agendaService.mandaAvisoNaoTomouMedicamento(idAgenda);
    }

    @ApiMethod(name = "gravarDosagem", path = "alterardosagem/{idAgenda}/{dosagem}", httpMethod = ApiMethod.HttpMethod.POST)
    public void gravarDosagem(@Named("idAgenda") Long idAgenda, @Named("dosagem") Integer dosagem) throws ConflictException, NotFoundException {
        agendaService.alterarDosagem(idAgenda, dosagem);
    }
}

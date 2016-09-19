package com.ciandt.inovasas.drugs_dispenser.services.endpoints;

import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
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
        name = "dispenser",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "services.drugs_dispenser.inovasas.ciandt.com",
                ownerName = "services.drugs_dispenser.inovasas.ciandt.com",
                packagePath = ""
        )
)
public class DispenserEndpoint {

    private DispenserService dispenserService;

    public DispenserEndpoint() {
        dispenserService = new DispenserService();
    }

    //getAgenda
    //setAgenda
    //insertDispenser
    //deleteDispenser

    @ApiMethod(name = "get", path = "get", httpMethod = "GET")
    public List<Dispenser> get(@Nullable @Named("codigo") String codigo) throws NotFoundException {
        if (codigo == null)
            return dispenserService.list();
        else
            return dispenserService.list(codigo);
    }

    @ApiMethod(name = "getById", path = "get/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public Dispenser getById(@Named("id") Long id) throws NotFoundException {
        return dispenserService.getById(id);
    }

    @ApiMethod(name = "getByCarteirinha", path = "carteirinha/{carteirinha}", httpMethod = ApiMethod.HttpMethod.GET)
    public Dispenser getByCarteirinha(@Named("carteirinha") String carteirinha) throws NotFoundException {
        return dispenserService.getByCarteirinha(carteirinha);
    }

    @ApiMethod(name = "getByCodigo", path = "codigo/{codigo}", httpMethod = ApiMethod.HttpMethod.GET)
    public Dispenser getByCodigo(@Named("codigo") String codigo) throws NotFoundException {
        return dispenserService.getByCodigoDispenser(codigo);
    }

    @ApiMethod(name = "post", path = "new", httpMethod = ApiMethod.HttpMethod.POST)
    public Dispenser post(Dispenser item) throws ConflictException, NotFoundException {
        return dispenserService.insert(item);
    }

    @ApiMethod(name = "put", path = "update", httpMethod = ApiMethod.HttpMethod.PUT)
    public Dispenser put(Dispenser item) throws NotFoundException, ConflictException {
        return dispenserService.update(item);
    }

    @ApiMethod(name = "delete", path = "delete/{id}", httpMethod = ApiMethod.HttpMethod.DELETE)
    public void delete(@Named("id") Long id) throws NotFoundException, ConflictException {
        dispenserService.remove(id);
    }
}

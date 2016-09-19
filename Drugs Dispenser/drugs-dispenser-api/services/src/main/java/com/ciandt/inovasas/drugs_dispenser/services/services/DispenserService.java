package com.ciandt.inovasas.drugs_dispenser.services.services;

import com.ciandt.inovasas.drugs_dispenser.services.daos.DispenserDao;
import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by rodrigosclosa on 30/06/16.
 */

public class DispenserService {

    private DispenserDao dispenserDao;

    public DispenserService() {
        dispenserDao = new DispenserDao();
    }

    public List<Dispenser> list() {
        return dispenserDao.listAll();
    }

    public List<Dispenser> list(String codigo) throws NotFoundException {

        List<Dispenser> list = dispenserDao.listByProperty("codigo", codigo);

        if(list == null || list.size() < 1) {
            throw new NotFoundException(String.format("Dispenser não localizado para o código: %s", codigo));
        }

        return list;
    }

    public Dispenser getById(Long id) throws NotFoundException {
        Dispenser item = dispenserDao.getByKey(id);

        if(item == null) {
            throw new NotFoundException("Reembolso não encontrado");
        }

        return item;
    }

    public Dispenser getByCarteirinha(String carteirinha) throws NotFoundException {
        Dispenser item = dispenserDao.getByProperty("carteirinhaAssociado", carteirinha);

        if(item == null) {
            throw new NotFoundException("Dispenser não localizado para esta carteirinha!");
        }

        return item;
    }

    public Dispenser getByCodigoDispenser(String codigo) throws NotFoundException {
        Dispenser item = dispenserDao.getByProperty("codigo", codigo);

        if(item == null) {
            throw new NotFoundException("Dispenser não localizado para este código!");
        }

        return item;
    }

    public Dispenser insert(Dispenser item) throws ConflictException, NotFoundException {
        if(item == null)
        {
            throw new ConflictException("Dados do dispenser não informados.");
        }
        else if(item.getCodigo() == null)
        {
            throw new ConflictException("Código do dispenser não informado.");
        }
        else if(item.getDataCadastro() == null) {
            throw new ConflictException("Data de cadastro não informada.");
        }
        else if(item.getNomeDispenser() == null) {
            throw new ConflictException("Nome do dispenser não localizado.");
        }

        dispenserDao.insert(item);

        return item;
    }

    public Dispenser update(Dispenser item) throws ConflictException, NotFoundException {

        if(item == null)
        {
            throw new ConflictException("Dados do dispenser não informados.");
        }
        else if(item.getCodigo() == null)
        {
            throw new ConflictException("Código do dispenser não informado.");
        }
        else if(item.getDataCadastro() == null) {
            throw new ConflictException("Data de cadastro não informada.");
        }
        else if(item.getNomeDispenser() == null) {
            throw new ConflictException("Nome do dispenser não localizado.");
        }

        Dispenser u = dispenserDao.getById(item.getId());

        if(u == null) {
            throw new NotFoundException("Dispenser não encontrado.");
        }

        dispenserDao.update(item);

        return item;
    }

    public void remove(long id) throws ConflictException, NotFoundException {
        Dispenser item = dispenserDao.getByKey(id);

        if(item == null) {
            throw new NotFoundException("Dispenser não encontrado.");
        }

        dispenserDao.delete(item);
    }

}

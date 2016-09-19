package com.ciandt.inovasas.drugs_dispenser.services.utils;

import com.ciandt.inovasas.drugs_dispenser.services.models.Agenda;
import com.ciandt.inovasas.drugs_dispenser.services.models.Dispenser;
import com.ciandt.inovasas.drugs_dispenser.services.models.Medicamento;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by rodrigosclosa on 29/12/15.
 */
public class OfyService {

    static {
        ObjectifyService.register(Medicamento.class);
        ObjectifyService.register(Dispenser.class);
        ObjectifyService.register(Agenda.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}
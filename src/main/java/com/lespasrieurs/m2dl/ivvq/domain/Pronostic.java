package com.lespasrieurs.m2dl.ivvq.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Marti_000 on 30/03/2018.
 */
@Entity
public class Pronostic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_pronostic;

    @NotNull
    Date date;

    @NotNull
    @Min(0)
    private int prono_domicile;

    @NotNull
    @Min(0)
    private int prono_exterieur;

    public Long getId_pronostic() {
        return id_pronostic;
    }

    public void setId_pronostic(Long id_pronostic) {
        this.id_pronostic = id_pronostic;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getProno_domicile() {
        return prono_domicile;
    }

    public void setProno_domicile(int prono_domicile) {
        this.prono_domicile = prono_domicile;
    }

    public int getProno_exterieur() {
        return prono_exterieur;
    }

    public void setProno_exterieur(int prono_exterieur) {
        this.prono_exterieur = prono_exterieur;
    }

    private int CalculScore(){

        return (0);
    };
}

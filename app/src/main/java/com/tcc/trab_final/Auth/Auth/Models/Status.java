package com.tcc.trab_final.Auth.Auth.Models;

public class Status {
    private String situacao;
    private String totalPosse;
    private String totalMembros;
    private String idLegislatura;
    private Lider lider;

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTotalPosse() {
        return totalPosse;
    }

    public void setTotalPosse(String totalPosse) {
        this.totalPosse = totalPosse;
    }

    public String getTotalMembros() {
        return totalMembros;
    }

    public void setTotalMembros(String totalMembros) {
        this.totalMembros = totalMembros;
    }

    public String getIdLegislatura() {
        return idLegislatura;
    }

    public void setIdLegislatura(String idLegislatura) {
        this.idLegislatura = idLegislatura;
    }

    public Lider getLider() {
        return lider;
    }

    public void setLider(Lider lider) {
        this.lider = lider;
    }

    public static class Lider {

        private String siglaPartido;
        private String nome;
        private String urlFoto;

        public String getNome() {
            return nome;
        }

        public String getSiglaPartido() {
            return siglaPartido;
        }

        public void setSiglaPartido(String siglaPartido) {
            this.siglaPartido = siglaPartido;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getUrlFoto() {
            return urlFoto;
        }

        public void setUrlFoto(String urlFoto) {
            this.urlFoto = urlFoto;
        }
    }
}


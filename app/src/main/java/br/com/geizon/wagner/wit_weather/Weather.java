package br.com.geizon.wagner.wit_weather;

public class Weather {

    private String cidade;
    private String local;
    private String temperatura;
    private String clima;
    private String tempmax;
    private String tempmin;
    private String sensacao;
    private String umidade;
    private String vento;
    private String presao;
    private String visibilidade;
    private String Imagemclima;
    private int backgroud;

    public Weather(String cidade, String local, String temperatura, String clima, String tempmax,
                   String tempmin, String sensacao, String umidade, String vento,
                   String presao, String visibilidade, String imagemclima, int backgroud) {
        this.cidade = cidade;
        this.local = local;
        this.temperatura = temperatura;
        this.clima = clima;
        this.tempmax = tempmax;
        this.tempmin = tempmin;
        this.sensacao = sensacao;
        this.umidade = umidade;
        this.vento = vento;
        this.presao = presao;
        this.visibilidade = visibilidade;
        Imagemclima = imagemclima;
        this.backgroud = backgroud;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTempmax() {
        return tempmax;
    }

    public void setTempmax(String tempmax) {
        this.tempmax = tempmax;
    }

    public String getTempmin() {
        return tempmin;
    }

    public void setTempmin(String tempmin) {
        this.tempmin = tempmin;
    }

    public String getSensacao() {
        return sensacao;
    }

    public void setSensacao(String sensacao) {
        this.sensacao = sensacao;
    }

    public String getUmidade() {
        return umidade;
    }

    public void setUmidade(String umidade) {
        this.umidade = umidade;
    }

    public String getVento() {
        return vento;
    }

    public void setVento(String vento) {
        this.vento = vento;
    }

    public String getPresao() {
        return presao;
    }

    public void setPresao(String presao) {
        this.presao = presao;
    }

    public String getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
    }

    public String getImagemclima() {
        return Imagemclima;
    }

    public void setImagemclima(String imagemclima) {
        Imagemclima = imagemclima;
    }

    public int getBackgroud() {
        return backgroud;
    }

    public void setBackgroud(int backgroud) {
        this.backgroud = backgroud;
    }
}

public class Correo {
    private String id;
    private boolean flagSpam;
    private boolean correoInicial;
    private boolean correoFinal;
    private int cuarentenaTiempo;

    public Correo(String id, boolean flagSpam, boolean correoInicial, boolean correoFinal, int cuarentenaTimepo){
        this.id = id;
        this.flagSpam = flagSpam;
        this.correoInicial = correoInicial;
        this.correoFinal = correoFinal;
        this.cuarentenaTiempo = cuarentenaTimepo;
    }

    public boolean getflagSpam(){
        return flagSpam;
    }

    public boolean coIncial(){
        if(correoInicial== true){
            return true;
        } else{
            return false;
        }
    }

    public boolean Cofinal(){
        if(correoFinal == true){
            return true;
        } else{
            return false;
        }
    }

    public void setflagSpam(){
        if((coIncial() == true || Cofinal() == true) && flagSpam == true){
            this.flagSpam = false;
        }
    }

    public void setcuarentenaTiempo(int t){
        this.cuarentenaTiempo = t;
    }

    public int getcuarentenaTiempo(){
        return cuarentenaTiempo;
    }

    public void discuarentenaTiempo(){
        cuarentenaTiempo--;
    }

    public String getId(){
        return id;
    }

}

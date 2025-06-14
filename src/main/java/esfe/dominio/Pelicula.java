package esfe.dominio;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private String genero;

    public Pelicula() {

    }

    public Pelicula(int id, String titulo, String director, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}

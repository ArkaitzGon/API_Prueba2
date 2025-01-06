package service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Cartelera;
import com.example.domain.Cine;
import com.example.domain.Pelicula;
import com.example.dto.CineCartelera;
import com.example.dto.PeliculaCartelera;
import com.example.repository.CarteleraRepository;
import com.example.repository.CineRepository;
import com.example.repository.PeliculaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
@Service
public class CarteleraService {
    @Autowired
    private CineRepository repository;

    @Autowired
    private PeliculaRepository peRepository;

    @Autowired
    private CarteleraRepository caRepository;
    
    
    public List<CineCartelera> getCartelera(){
        List<CineCartelera> values = new ArrayList<>();
        List<Cine> cines = repository.findAll();
        cines.forEach(cine ->{
                CineCartelera cineCartelera =  new CineCartelera(
                        cine.getId(),
                        cine.getLatitud(),
                        cine.getLongitud(),
                        cine.getNombre()
                );
                cineCartelera.setPeliculas(this.getPeliculas(cine.getId()));
                values.add(cineCartelera);
        });
        return values;
    }

    public List<PeliculaCartelera> getPeliculas(int cineId){
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        
        List<PeliculaCartelera> peliculas = new ArrayList<>();
        List<Cartelera> cartelera = caRepository.findByCineIdAndFecha(
                cineId, fechaFormateada
        );
        if(!cartelera.isEmpty()){
            cartelera.forEach(cart ->{
                Pelicula pelicula = peRepository.findById(cart.getPeliculaId()).orElse(null);
                if(pelicula != null){
                    PeliculaCartelera pc = new PeliculaCartelera(
                            pelicula.getId(),
                            pelicula.getTitulo(),
                            pelicula.getImagen(),
                            pelicula.getAnchoImagen(),
                            pelicula.getAltoImagen()
                            );
                    pc.setHorario(cart.getHorario().split(","));
                    peliculas.add(pc);
                }
            });
        }
        return peliculas;
    }
    

    /**
     * Busca las peliculas
     * @return
     */
    public List<CineCartelera> cartelera(){
        List<Cine> cines = repository.findAll();
        if(cines == null || cines.isEmpty())
            return new ArrayList<>();

        List<CineCartelera> cartelera = new ArrayList<>();
        RemoteWebDriver driver = new FirefoxDriver(new FirefoxOptions());

        cines.forEach(cine ->{
            CineCartelera cineCartelera = new CineCartelera(
                    cine.getId(),
                    cine.getLatitud(),
                    cine.getLongitud(),
                    cine.getNombre()
            );
            List<PeliculaCartelera> peliculas = new ArrayList<>();
            System.out.println(cine.getNombre());
            driver.get(cine.getUrl());
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Iterator<WebElement> iterator = driver
                    .findElements(By.className("movie"))
                    .iterator();
            //Itera sobre todas las peliculas del cine
            while (iterator.hasNext()) {
                WebElement element = iterator.next();
                wait.until(d -> element.isDisplayed());
                String[] horario = buscarCine(
                        element.findElements(By.className("movie-showtimes-n"))
                );
                if(horario.length > 0){
                    String titulo = element.findElement(By.className("mc-title")).getText();
                    PeliculaCartelera pelicula = buscarPelicula(titulo);
                    System.out.println("HORARIO::"+horario.length);
                    if(pelicula != null){
                        pelicula.setHorario(horario);
                        peliculas.add(pelicula);
                    }
                }
            }
            cineCartelera.setPeliculas(peliculas);
            cartelera.add(cineCartelera);
        });
        driver.quit();
        return cartelera;
    }
    /**
     * Dado un elemento Web obtiene los horarios
     * @param sessions
     * @return
     */
    private String[] buscarCine(List<WebElement> sessions) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Iterator<WebElement> iterator = sessions.iterator();
        String[] horarios = new String[0];
        while (iterator.hasNext()){
            WebElement session = iterator.next();
            if(session.findElement(By.className("mv-title"))
                    .getText().contains("Español")) {
                WebElement element = null;
                Iterator<WebElement> it = session.findElements(By.className("g-0")).iterator();
                while (it.hasNext()){
                    element = it.next();
                    String date = element.getAttribute("data-sess-date");
                    if(date != null && date.equals(today))
                        break;
                }

                if(element !=null ) {
                    String [] h = session.findElement(By.className("g-0"))
                            .findElement(By.className("sess-times"))
                            .getText().split("\n");

                    System.out.println(h.length);
                    System.out.println(session.findElement(By.className("g-0"))
                            .findElement(By.className("sess-times")));
                    horarios = h;
                }
            }
        }
        return horarios;
    }

    /**
     * Busca la pelicula en la Base de datos y sino Existe la busca en wikipedia
     * @param titulo
     * @return
     */
    private PeliculaCartelera buscarPelicula(String titulo) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Pelicula pelicula;
        Optional<Pelicula> optional = peRepository.findByTituloAndFechaEstreno(titulo,year);
        if(optional.isPresent()){
            pelicula = optional.get();
        }

        else {
            String url = search(titulo);
            if (!url.contains("https://es.wikipedia.org/wiki/"))
                url = "https://es.wikipedia.org" + url;
            pelicula = info(url);
            //System.out.println("PE::"+pelicula.getTitulo());
            if(pelicula != null)
                peRepository.save(pelicula);
        }
        if(pelicula == null)
            return null;
        else
           return new PeliculaCartelera(
                   pelicula.getId(),
                   pelicula.getTitulo(),
                   pelicula.getImagen(),
                   pelicula.getAnchoImagen(),
                   pelicula.getAltoImagen()
           );

    }

    /**
     * Realiza la peticion Http
     * @param url
     * @return
     */
    public static HtmlPage scrape(String url ) {

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {
            return client.getPage(url);
        } catch (Exception e) {

            //e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca las palabras clave en wikipedia y devuelve el enlace al primer resultado
     * @param titulo
     * @return
     */
    public static String search(String titulo) {

        //titulo = titulo.replace(' ', '+');
        String url = "https://es.wikipedia.org/w/index.php?search=" + titulo +
                "&title=Especial:Buscar&ns0=1&ns100=1&ns104=1";
        HtmlPage  page = scrape(url);


        if(page.getBaseURL().toString().contains("https://es.wikipedia.org/wiki/"))
            return page.getBaseURL().toString();

        if(page == null)
            return "";

        List<HtmlElement> items = page.getByXPath("//li[@class='mw-search-result mw-search-result-ns-0']");
        if(!items.isEmpty()) {
            List<HtmlElement> values = items.get(0).getByXPath("//div[@class='mw-search-result-heading']");
            //System.out.println("values:"+values.size());
            if(!values.isEmpty()) {
                HtmlAnchor anchor = ((HtmlAnchor) values.get(0).getFirstByXPath(".//a"));
                //System.out.println("anchor:"+anchor.getHrefAttribute());
                return anchor.getHrefAttribute();
            }
        }
        return "";
    }

    /**
     * Dada la url de la pelicula
     * @param url
     * @return
     */
    public static Pelicula info(String url) {
        try {
            HtmlPage page = scrape( url);
            int position = 1;
            List<HtmlElement> items = page.getByXPath("//th[@class='cabecera cine']");
            if(!items.isEmpty()) {
                Pelicula film = new Pelicula();
                items = page.getByXPath(".//table[contains(@class, 'infobox')]/tbody/tr");

                //Imagen with y height
                if(items.get(position).getFirstByXPath(".//td[contains(@class, 'imagen')]") !=null ){

                    HtmlImage img = items.get(position).getFirstByXPath(".//td[contains(@class, 'imagen')]//img");
//                    System.out.println("img:"+img+"::");
                    if(img != null) {
                        film.setImagen(img.getSrc());
                        film.setAltoImagen(Integer.parseInt(img.getHeightAttribute()));
                        film.setAnchoImagen(Integer.parseInt(img.getWidthAttribute()));
//                        System.out.println("IMG:"+img.getSrc());
//                        System.out.println("IMG_height:"+img.getHeightAttribute());
//                        System.out.println("IMG_width:"+img.getWidthAttribute());
                        position++;
                    }
                }
                //Titulo
                HtmlElement title = items.get(position).getFirstByXPath(".//td");
                //System.out.println("TIT:"+title.getTextContent());
                film.setTitulo(title.getTextContent());

                for (HtmlElement item : items) {
                    HtmlAnchor a = item.getFirstByXPath(".//th/a");
                    if(a != null) {
                        switch (a.getAttribute("title")) {
                            //Protagonistas
                            case "Actuación":
                                List<HtmlAnchor> elements = item.getByXPath(".//td/a");
                                for (HtmlElement element : elements) {
                                    //System.out.println("PROT:"+element.getTextContent());
                                    film.setReparto(element.getTextContent().join(", "));
                                }
                                break;
                            //Año
                            case "Año":
                                HtmlElement e = item.getFirstByXPath(".//td");
                                //System.out.println("AÑO:"+e.getTextContent().trim());
                                film.setFechaEstreno(Integer.parseInt(e.getTextContent().trim()));
                                break;
                            //Generos
                            case "Género cinematográfico":
                                elements = item.getByXPath(".//td/a");
                                for (HtmlElement element : elements) {
                                    //System.out.println("GEN:"+element.getTextContent());
                                    film.setGenero(element.getTextContent().join(", "));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }

                //Resumen
                HtmlElement paragraph =page.getFirstByXPath(".//div[@id='mw-content-text']//p");
                film.setResumen(paragraph.getTextContent());
                //System.out.println("RESUMEN"+paragraph.getTextContent());
                return film;
            }
            return null;
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * Dada un ruta de un fichero json carga los cines en la bd
     * @param jsonPath
     */
    public void cargarCines(String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Cine>> typeReference = new TypeReference<List<Cine>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/"+jsonPath);
        try {
            List<Cine> cines = mapper.readValue(inputStream,typeReference);
            for (Cine cine : cines) {
                Cine c = repository.save(cine);
                //System.out.println(c.getNombre());
            }
            System.out.println("Users Saved!");
        } catch (IOException e){
            System.out.println("Unable to save users: " + e.getMessage());
        }
    }


}
package at.htl.feature;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedList;

@ApplicationScoped
@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    MeterRegistry registry;

    private LinkedList<String> list = new LinkedList<>();

    @Inject
    PersonRepository personRepository;

    public PersonResource(MeterRegistry registry) {
        this.registry = registry;
        registry.gaugeCollectionSize("person_list_size", Tags.empty(), list);
    }

    @GET
    @Path("/counter/check/{input}")
    public boolean checkPersonCounter(@PathParam("input") String input) {
        list.add(input);
        registry.counter("person_counter").increment();
        return getPerson(input);  // Use getPerson instead of addPerson
    }

    @GET
    @Path("/timer/check/{input}")
    public boolean checkPersonTimer(@PathParam("input") String input) {
        list.add(input);
        Timer.Sample sample = Timer.start(registry);
        boolean result = getPerson(input);  // Use getPerson instead of addPerson
        sample.stop(registry.timer("person_timer"));
        return result;  // Use getPerson instead of addPerson
    }

    // New method to check if a person exists in the database
    public boolean getPerson(String input) {
        String[] parts = input.split(",");
        if (parts.length != 2 || input.isEmpty()) {
            return false;  // Input format is not correct
        }

        String name = parts[0].trim();
        int age = Integer.parseInt(parts[1].trim());

        // Check if the person exists in the repository
        Person person = (Person) personRepository.find("p_name = ?1 and p_age = ?2", name, age);

        return person != null;  // Return true if person exists, false otherwise
    }

    @DELETE
    @Path("/clear-list")
    public void clearList() {
        list.clear();
    }
}

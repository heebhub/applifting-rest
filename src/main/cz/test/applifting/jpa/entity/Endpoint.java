package cz.test.applifting.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endpoint {

     @Id
     @Column(name = "id")
     @SequenceGenerator(name = "endpoint_sequence", sequenceName = "endpoint_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "endpoint_sequence")
     private Long id;

     @Column(name = "name")
     @Size(min = 2, max = 45)
     private String name;

     @Column(name = "url")
     @URL
     private String url;

     @Column(name = "time_interval")
     private Integer timeInterval;

     @Column(name = "created_on", updatable = false)
     @CreationTimestamp
     private Date createdOn;

     @Column(name = "last_visited", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
     @UpdateTimestamp
     private Date lastVisited;

     @JsonIgnore
     @ToString.Exclude
     @EqualsAndHashCode.Exclude
     //see https://stackoverflow.com/questions/17445657/hibernate-onetomany-java-lang-stackoverflowerror/55425076
     @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     private User user;
}

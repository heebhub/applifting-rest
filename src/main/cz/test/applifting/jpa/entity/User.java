package cz.test.applifting.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

     @Id
     @Column(name = "id")
     @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
     private Long id;

     @Min(3)
     @Max(100)
     @NotBlank
     @ColumnDefault("username")
     private String username;

     @Email
     @NotBlank
     @ColumnDefault("email")
     private String email;

     @Pattern(regexp = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
     @NotBlank
     @Column(name = "uuid")
     private String token;

     @OneToMany(mappedBy = "user")
     @JsonIgnore
     @ToString.Exclude
     @EqualsAndHashCode.Exclude
     private Set<Endpoint> endpoints;
}

package cz.test.applifting.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

     @Id
     @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "result_sequence")
     @SequenceGenerator(name = "result_sequence", sequenceName = "result_sequence", allocationSize = 1)
     private Long id;

     @UpdateTimestamp
     @Column(name = "updated_on")
     private Date updatedOn;

     @Column(name = "status_code")
     private Integer statusCode;

     @Column(name = "payload")
     private String payload;

     @JoinColumn(name = "endpoint_id", nullable = false)
     @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
     private Endpoint endpoint;

     public Result(Integer statusCode, String payload, Endpoint endpoint) {
          this.statusCode = statusCode;
          this.payload = payload;
          this.endpoint = endpoint;
     }
}

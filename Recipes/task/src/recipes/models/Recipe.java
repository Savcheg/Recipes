package recipes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.services.UserDetailsImpl;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Entity
@Table(name = "RECIPES")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "NAME")
    @NotBlank
    private String name;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "date")
    @UpdateTimestamp
    @NotNull
    private LocalDateTime date;

    @Column(name = "DESCRIPTION")
    @NotBlank
    private String description;

    @Column(name = "INGREDIENTS")
    @NotNull
    @Size(min = 1)
    private String[] ingredients;

    @Column(name = "DIRECTIONS")
    @NotNull
    @Size(min = 1)
    private String[] directions;

    @JsonIgnore
    private String author;

    public Recipe() {
        this.date = LocalDateTime.now();
    }

    public Recipe(Long id, String name, String category, LocalDateTime date, String description, @NotNull @Size(min = 1) String[] ingredients, @NotNull @Size(min = 1) String[] directions) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.date = LocalDateTime.now();
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate() {
        this.date = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getDirections() {
        return directions;
    }

    public void setDirections(String[] directions) {
        this.directions = directions;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

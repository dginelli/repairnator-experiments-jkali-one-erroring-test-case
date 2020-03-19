package com.kh013j.model.repository;

import com.kh013j.model.domain.Category;
import com.kh013j.model.domain.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByCategory(Category category);

    List<Dish> findByCategoryOrderByPrice(Category category);

    List<Dish> findByCategoryOrderByCalories(Category category);

    List<Dish> findByCategoryOrderByPreparingtime(Category category);

    List<Dish> findByNameContainingIgnoreCase(String name);

    //Change to nativequery = false, so that would work for any DB;
    @Query(value = "SELECT rh.dish.* from rh.dish" +
            "  INNER JOIN rh.orderdish ON rh.orderdish.dish_id = rh.dish.id" +
            "  WHERE rh.orderdish.order_id = ALL(SELECT rh.order.id  FROM rh.order" +
            "  INNER JOIN rh.orderdish ON rh.orderdish.order_id = rh.order.id" +
            "  WHERE rh.orderdish.dish_id = ?1) AND rh.dish.id != ?1 " +
            "  GROUP BY rh.dish.id" +
            "  ORDER BY COUNT(rh.dish.*) DESC " +
            "  LIMIT 10; ", nativeQuery = true)
    List<Dish> findDishByPopularCustomQuery(Long id);
}
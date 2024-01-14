package ru.clevertec.ecl.data.repository.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriteriaQueryBuilder<T> {

    @PersistenceContext
    private EntityManager manager;

    public TypedQuery<T> search(Class<T> clazz, String param) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        Order asc = null;
        for (Field field : clazz.getDeclaredFields()) {
            String fieldClassName = field.getType().getSimpleName();
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            String columnName = column.name();
            boolean annotationIdPresent = field.isAnnotationPresent(Id.class);
            if (annotationIdPresent) {
                asc = cb.asc(root.get(columnName));
            }
            boolean predicateConditionString = fieldClassName.equalsIgnoreCase("string");
            boolean predicateConditionNumber = fieldClassName.equalsIgnoreCase("integer")
                    || fieldClassName.equalsIgnoreCase("long");
            if (predicateConditionString) {
                Predicate predicate = cb.like(root.get(field.getName()), "%" + param + "%");
                predicates.add(predicate);
            } else if (predicateConditionNumber) {
                Expression<String> fieldAsString = root.get(field.getName()).as(String.class);
                Predicate predicate = cb.like(fieldAsString, "%" + param + "%");
                predicates.add(predicate);
            }
        }
        Predicate finalPredicate = cb.or(predicates.toArray(new Predicate[]{}));
        criteriaQuery.orderBy(asc);
        criteriaQuery.where(finalPredicate);
        return manager.createQuery(criteriaQuery);
    }
}

package com.horse.data.repository.horse;

import com.horse.data.entity.Horse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class HorseRepositoryImpl implements HorseCustomRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Horse> findByTrainerIdAndYear(Integer trainerId, Integer year) {

        StringBuilder sql = new StringBuilder();
        sql.append("Select h from Horse h where 0 = 0");

        if (trainerId != null) {
            sql.append(" And h.trainer.id = :trainerId");
        }

        if (year != null) {
            sql.append(" And YEAR(h.foaled) = :year");
        }

        TypedQuery<Horse> query = entityManager.createQuery(sql.toString(), Horse.class);

        if (trainerId != null) {
            query.setParameter("trainerId", trainerId);
        }

        if (year != null) {
            query.setParameter("year", year);
        }

        return query.getResultList();
    }
}

package com.hibernate.example.hotel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import com.hibernate.example.hotel.entity.Hotel;
import com.hibernate.example.hotel.entity.Suite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HotelTests {
    private static EntityManagerFactory sEntityManager;
    private EntityManager em = sEntityManager.createEntityManager();

    @BeforeClass
    public static void setUp() {
        sEntityManager = Persistence.createEntityManagerFactory("hotel_persistence_unit");
        EntityManager em = sEntityManager.createEntityManager();
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(getHotel("Old hotel", 2, 5));
        hotels.add(getHotel("Harmony", 4, 10));
        hotels.add(getHotel("Huston", 5, 6));
        em.getTransaction().begin();
        hotels.forEach(hotel -> em.persist(hotel));
        em.getTransaction().commit();
        em.close();
    }

    @AfterClass
    public static void destroy() {
        sEntityManager.close();
    }

    @Test
    public void testFindUsingNamedQuery() {
        Suite suite = em.createNamedQuery("findSuite", Suite.class).setParameter("bedsNumber", 3)
                .setParameter("hotelName", "Huston")
                .getSingleResult();
        assertEquals(3, suite.getBedsNumber());
        assertEquals(3, suite.getRoomNumber());
        System.out.println(suite);
    }

    @Test
    public void testSuiteBooking() {
        List<Suite> suites = em.createNativeQuery("select * from public.suite where beds_count = 2", Suite.class)
                .getResultList();
        em.getTransaction().begin();
        suites.forEach(suite -> suite.setBooked(true));
        em.flush();
        em.getTransaction().commit();
        Suite suite = em.createNamedQuery("findSuite", Suite.class).setParameter("bedsNumber", 2)
                .setParameter("hotelName", "Huston")
                .getSingleResult();
        assertTrue(suite.isBooked());
    }

    @Test(expected = OptimisticLockException.class)
    public void testOptimisticLock() {
        Suite fiveBedSuite_1 = em.createNamedQuery("findSuite", Suite.class)
                .setParameter("bedsNumber", 5)
                .setParameter("hotelName", "Harmony")
                .getSingleResult();
        Suite fiveBedSuite_2 = em.createNamedQuery("findSuite", Suite.class)
                .setParameter("bedsNumber", 5)
                .setParameter("hotelName", "Harmony")
                .getSingleResult();
        em.detach(fiveBedSuite_1);
        em.detach(fiveBedSuite_2);

        em.getTransaction().begin();
        fiveBedSuite_1.setDescription("New description");
        em.merge(fiveBedSuite_1);
        em.getTransaction().commit();

        em.getTransaction().begin();
        fiveBedSuite_2.setBooked(true);
        em.merge(fiveBedSuite_2);
        em.getTransaction().commit();
    }

    @Test
    public void testGetHotelByCriteria() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Hotel> root = query.from(Hotel.class);
        Join<Hotel, Suite> join = root.join("suites");
        query.select(root.get("name"));
        query.where(builder.equal(join.get("booked"), false));
        query.groupBy(root.get("name"));
        query.having(builder.gt(builder.count(join.get("roomNumber")), 5));
        List<String> hotels = em.createQuery(query).getResultList();
        hotels.forEach(hotel -> System.out.println(hotel));
        assertEquals(1, hotels.size());
    }

    private static Hotel getHotel(String name, int stars, int suitesCount) {
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setStars(stars);
        hotel.setDescription(String.format("Hotel info: %s, stars: %d", name, stars));
        hotel.setSuites(generateSuites(suitesCount, hotel));
        return hotel;
    }

    private static List<Suite> generateSuites(int count, Hotel hotel) {
        List<Suite> suites = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Suite suite = new Suite();
            suite.setRoomNumber(i);
            suite.setBedsNumber(count - i);
            suite.setDescription("Room " + i);
            suite.setHotel(hotel);
            suites.add(suite);
        }
        return suites;
    }
}

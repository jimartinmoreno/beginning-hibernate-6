package chapter11.servlets;

import chapter11.dto.UserDTO;
import chapter11.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

public class AddUserServletDTO extends AddUserServlet {
    protected UserDTO createUser(Session session, String userName) {
        UserDTO dto;
        try {
            Query<UserDTO> query = session.createQuery(
                    "select new chapter11.dto.UserDTO(u.id, u.name,u.active) "
                            + "from User u where u.name=:name",
                    UserDTO.class);
            query.setParameter("name", userName);

            dto = query.getSingleResult();
        } catch (NoResultException nre) {
            User u = new User(userName, true);
            session.save(u);
            dto = new UserDTO(u.getId(), u.getName(), u.isActive());

        }
        return dto;
    }

}

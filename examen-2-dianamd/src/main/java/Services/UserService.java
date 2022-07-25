package Services;

import Entities.UserApp;
import Enums.RolesApp;
import Exceptions.ObjectDoesNotExistException;
import org.hibernate.Hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService extends ManageDb<UserApp>{
    private static UserService instancia;
    private UserService(){
        super(UserApp.class);

    }

    public static UserService UserService(){
        if(instancia==null){
            instancia = new UserService();
        }
        return instancia;
    }
    public List<UserApp> getAllUsers(){
        List<UserApp> usersInitialList =  this.findAll();
        List<UserApp> users = usersInitialList.stream()
                .filter(p -> p.isDeleted() ==false).collect(Collectors.toList());
        return users;
    }

    public UserApp getUserById(long id){
        UserApp user= this.find(id);
        if(user!=null && user.isDeleted()==false){
            return user;
        }
        return null;
    }
    public UserApp createUser(UserApp user){
        if(user!=null && getUserById(user.getId())!=null){
            System.out.println("El Usuario que desea agregar ya existe");
            return null; //generar una excepcion...
        }
        this.create(user);
        return user;
    }
    public UserApp updateUser(UserApp user){
        UserApp tmp = getUserById(user.getId());
        if(tmp == null){//no existe, no puede se actualizado
            throw new ObjectDoesNotExistException("No Existe el usuario: "+user.getId());
        }
        this.modify(user);
        return user;
    }
    public boolean deleteUserById(long id){
        UserApp tmp = this.getUserById(id);
        tmp.setDeleted(true);
        this.updateUser(tmp);
        return true;
    }

    public UserApp getUserByUser(String user) {
        List<UserApp> users=this.getAllUsers();
        for(int i=0;i<users.size();i++){
            if(users.get(i).getUsername().equalsIgnoreCase(user)){
                UserApp userToBeReturned= users.get(i);
                return userToBeReturned;
            }
        }
        System.out.println("Este usuario no existe");
        return null;
    }

    public boolean isAdmin(UserApp user){
        System.out.println("antes del iterator");
        Set<RolesApp> roles = user.getRoles();
        Hibernate.initialize(roles);
        for (RolesApp s : roles) {
            if(s.equals(RolesApp.ROLE_ADMIN)){
                return true;
            }

        }

        return false;
    }

}

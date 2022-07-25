package Services;

import Entities.FormApp;
import Entities.UserApp;
import Exceptions.ObjectDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormService extends ManageDb<FormApp>{
    private static FormService instancia;
    private FormService(){
        super(FormApp.class);
    }

    public static FormService FormService(){
        if(instancia==null){
            instancia = new FormService();
        }
        return instancia;
    }
    public List<FormApp> getAllForms(){
        List<FormApp> formsInitialList =  this.findAll();
        List<FormApp> forms = formsInitialList.stream()
                .filter(p -> p.isDeleted() ==false).collect(Collectors.toList());
        return forms;
    }

    public FormApp getFormById(long id){
        FormApp form= this.find(id);
        if(form!=null && form.isDeleted()==false){
            return form;
        }
        return null;
    }
    public FormApp createForm(FormApp form){
        if(form!=null && getFormById(form.getId())!=null){
            System.out.println("El Usuario que desea agregar ya existe");
            return null; //generar una excepcion...
        }
        this.create(form);
        return form;
    }
    public FormApp updateForm(FormApp form){
        FormApp tmp = getFormById(form.getId());
        if(tmp == null){//no existe, no puede se actualizado
            throw new ObjectDoesNotExistException("No Existe el usuario: "+form.getId());
        }
        this.modify(form);
        return form;
    }
    public boolean deleteFormById(long id){
        FormApp tmp = this.getFormById(id);
        tmp.setDeleted(true);
        this.updateForm(tmp);
        return true;
    }

    public List<FormApp> getFormsByUser(UserApp user) {
        List<FormApp> forms=this.getAllForms();
        List<FormApp> formsByUser=new ArrayList<>();
        for(int i=0;i<forms.size();i++){
            if(forms.get(i).getUserForm().getUsername().equals(user.getUsername())){
                formsByUser.add(forms.get(i));

            }
        }
        return formsByUser;
    }
}

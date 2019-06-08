package cz.cvut.fel.stepaego.JPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.apache.commons.lang3.StringUtils.isNumeric;

//Application class
//persist - Create, getReference - Read, merge - Update, remove - Delete
public class App {

    private int amountOfEntities = 0;
    private int amountOfRelations = 0;
    private int position = -1;
    private JFrame jf;
    private DefaultTableModel addEdtm, addRdtm, Edtm, Rdtm;
    private JTable addEntities, addRelations, lEntities, lRelations;
    private JScrollPane spAddEntities, spAddRelations, spEntities, spRelations;
    private EntityManagerFactory emf;
    private EntityManager em;
    private HashMap componentMap;

    public App() {
        //Entity Manager for working with data in database
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();

        //hashmap of all components in JFrame
        componentMap = new HashMap<String, Component>();

        //basic JFrame
        jf = new JFrame("JPA Application");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);                 //this will make
        jf.setUndecorated(true);                                    //application fullscreen
        jf.setVisible(true);

        int WIDTH = jf.getWidth();
        int HEIGHT = jf.getHeight();


        //button for exiting the app
        JButton exitButton = new JButton("Exit");
        exitButton.setLocation(WIDTH - 70, HEIGHT - 40);
        exitButton.setSize(60, 30);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                em.close();
                System.exit(0);
            }
        });

        //create headers for tables
        String[] Eheader = {"Pilot ID", "Name", "Surname", "Pass number", "Citizenship",
                "Email", "License number"};
        String[] Rheader = {"Pilot ID", "Name", "Surname", " - ", "Flight ID", "Place from",
                "Place to"};
        String[] addRheader = {"Pilot ID", " - ", "Flight ID"};
        String[][] data = {{}};

        //table for adding Entity to database
        addEdtm = new DefaultTableModel(data, Eheader);
        addEntities = new JTable(addEdtm);
        addEntities.getTableHeader().setReorderingAllowed(false);
        addEntities.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        spAddEntities = new JScrollPane(addEntities);
        spAddEntities.setLocation(10, 10);
        spAddEntities.setSize(WIDTH / 2 - 90, 40);

        //button for adding Entity to database
        JButton addEButton = new JButton("Add");
        addEButton.setLocation(WIDTH / 2 - 70, 10);
        addEButton.setSize(60, 40);
        addEButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkEntityValidityOnAdd() == 1) {
                    createEntity(WIDTH);
                }
            }
        });

        //table for adding Relation to database
        addRdtm = new DefaultTableModel(data, addRheader);
        addRelations = new JTable(addRdtm) {
            public boolean isCellEditable(int row, int column) {
                if (column == 1) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        addRelations.getTableHeader().setReorderingAllowed(false);
        addRelations.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        spAddRelations = new JScrollPane(addRelations);
        spAddRelations.setLocation(WIDTH / 2 + 10, 10);
        spAddRelations.setSize(WIDTH / 2 - 90, 40);

        //button for adding Relation to database
        JButton addRButton = new JButton("Add");
        addRButton.setLocation(WIDTH - 70, 10);
        addRButton.setSize(60, 40);
        addRButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkRelationValidity() == 1) {
                    createRelation(WIDTH);
                }
            }
        });

        //create table for showing Entities, lEntities stands for list Entities
        Edtm = new DefaultTableModel(null, Eheader);
        lEntities = new JTable(Edtm) {
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };
        lEntities.getTableHeader().setReorderingAllowed(false);
        lEntities.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        spEntities = new JScrollPane(lEntities);
        spEntities.setLocation(10, 60);
        spEntities.setSize(WIDTH / 2 - 160, HEIGHT - 110);

        //button for refreshing Entities table
        JButton refreshEbutton = new JButton("Refresh");
        refreshEbutton.setLocation(10, HEIGHT - 40);
        refreshEbutton.setSize(100, 30);
        refreshEbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshButtons(WIDTH);
            }
        });

        //create table for showing Relations, lEntities stands for list Relations
        Rdtm = new DefaultTableModel(null, Rheader);
        lRelations = new JTable(Rdtm) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lRelations.getTableHeader().setReorderingAllowed(false);
        lRelations.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        spRelations = new JScrollPane(lRelations);
        spRelations.setLocation(WIDTH / 2 + 10, 60);
        spRelations.setSize(WIDTH / 2 - 90, HEIGHT - 110);

        //button for refreshing Relations table
        JButton refreshRbutton = new JButton("Refresh");
        refreshRbutton.setLocation(WIDTH / 2 + 10, HEIGHT - 40);
        refreshRbutton.setSize(100, 30);
        refreshRbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshButtons(WIDTH);
            }
        });

        //add all elements to JFrame
        jf.add(exitButton);
        jf.add(spEntities);
        jf.add(refreshEbutton);
        jf.add(spRelations);
        jf.add(refreshRbutton);
        jf.add(spAddEntities);
        jf.add(addEButton);
        jf.add(spAddRelations);
        jf.add(addRButton);
        jf.repaint();
        createComponentMap();

        //load tables for the first time
        refreshEntities(WIDTH);
        refreshRelations(WIDTH);
    }

    private void createComponentMap() {
        //list of all components(buttons) with names
        Component[] components = jf.getContentPane().getComponents();
        for (int i = 0; i < components.length; i++) {
            componentMap.put(components[i].getName(), components[i]);
        }
    }

    private Component getComponentByName(String name) {
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else return null;
    }

    private void refreshEntities(int w) {
        //get all Entities
        em.getTransaction().begin();
        List<Pilot> listPilots = em.createQuery(
                "SELECT p FROM Pilot p").getResultList();
        em.getTransaction().commit();
        //resetting table
        Edtm.setRowCount(0);
        amountOfEntities = 0;

        //putting values in table
        for (Pilot pilot : listPilots) {
            Edtm.addRow(new Object[]{pilot.getPilotId(), pilot.getName(), pilot.getSurname(), pilot.getIdNumber(),
                    pilot.getCitizenship(), pilot.getEmail(), pilot.getPilotLicenseNumber()});
            assignButtons(w, pilot.getPilotId());
            amountOfEntities++;
        }
    }

    private void createEntity(int w) {
        //making temporary Entity
        Pilot tmp = new Pilot();
        tmp.setPilotId(Integer.parseInt(addEntities.getValueAt(0, 0).toString()));
        tmp.setName(addEntities.getValueAt(0, 1).toString());
        tmp.setSurname(addEntities.getValueAt(0, 2).toString());
        tmp.setIdNumber(Integer.parseInt(addEntities.getValueAt(0, 3).toString()));
        tmp.setCitizenship(addEntities.getValueAt(0, 4).toString());
        tmp.setEmail(addEntities.getValueAt(0, 5).toString());
        tmp.setPilotLicenseNumber(Integer.parseInt(addEntities.getValueAt(0, 6).toString()));

        //resetting fields of table
        for (int i = 0; i < 7; i++) {
            addEntities.setValueAt(null, 0, i);
        }

        //adding Entity to table
        Edtm.addRow(new Object[]{tmp.getPilotId(), tmp.getName(), tmp.getSurname(), tmp.getIdNumber(),
                tmp.getCitizenship(), tmp.getEmail(), tmp.getPilotLicenseNumber()});

        //adding buttons to work with Entity
        assignButtons(w, tmp.getPilotId());
        amountOfEntities++;

        //sending new Entity to database
        em.getTransaction().begin();
        em.persist(tmp);
        em.getTransaction().commit();
    }

    private void updateEntity(int w, int id, int position) {
        //making temporary Entity
        em.getTransaction().begin();
        Pilot tmp = em.getReference(Pilot.class, id);
        tmp.setName(lEntities.getValueAt(position, 1).toString());
        tmp.setSurname(lEntities.getValueAt(position, 2).toString());
        tmp.setIdNumber(Integer.parseInt(lEntities.getValueAt(position, 3).toString()));
        tmp.setCitizenship(lEntities.getValueAt(position, 4).toString());
        tmp.setEmail(lEntities.getValueAt(position, 5).toString());
        tmp.setPilotLicenseNumber(Integer.parseInt(lEntities.getValueAt(position, 6).toString()));

        //adding Entity to table
        lEntities.setValueAt(tmp.getName(), position, 1);
        lEntities.setValueAt(tmp.getSurname(), position, 2);
        lEntities.setValueAt(tmp.getIdNumber(), position, 3);
        lEntities.setValueAt(tmp.getCitizenship(), position, 4);
        lEntities.setValueAt(tmp.getEmail(), position, 5);
        lEntities.setValueAt(tmp.getPilotLicenseNumber(), position, 6);

        //sending new Entity to database
        em.merge(tmp);
        em.getTransaction().commit();

        //refresh buttons
        refreshButtons(w);
    }

    private void deleteEntity(int w, int id) {
        //delete Entity and related relations from database
        em.getTransaction().begin();
        Pilot tmp = em.getReference(Pilot.class, id);
        em.getTransaction().commit();
        pfId pfId = new pfId();
        pfId.setPilotPilotId(id);
        fpId fpId = new fpId();
        fpId.setPilotsPilotId(id);
        ArrayList<Flight> flightsToRemove = new ArrayList<Flight>();
        for (Flight flight : tmp.getFlights()) {
            flightsToRemove.add(flight);
        }
        for (Flight flight : flightsToRemove) {
            pfId.setFlightsFlightId(flight.getFlightId());
            fpId.setFlightFlightId(flight.getFlightId());
            deleteRelation(w, pfId, fpId, tmp, flight);
        }
        em.getTransaction().begin();
        em.remove(tmp);
        em.getTransaction().commit();

        //refresh buttons
        refreshButtons(w);
    }

    private void refreshRelations(int w) {
        //get all Relations
        em.getTransaction().begin();
        List<Pilot_flight> listPFs = em.createQuery(
                "SELECT pf FROM Pilot_flight pf").getResultList();
        em.getTransaction().commit();
        //resetting table
        Rdtm.setRowCount(0);
        amountOfRelations = 0;

        //putting values in table
        for (Pilot_flight pf : listPFs) {
            fpId fpId = new fpId();
            fpId.setFlightFlightId(pf.getPfId().getFlightsFlightId());
            fpId.setPilotsPilotId(pf.getPfId().getPilotPilotId());
            Rdtm.addRow(new Object[]{em.getReference(Pilot.class, pf.getPfId().getPilotPilotId()).getPilotId(),
                    em.getReference(Pilot.class, pf.getPfId().getPilotPilotId()).getName(),
                    em.getReference(Pilot.class, pf.getPfId().getPilotPilotId()).getSurname(), "-",
                    em.getReference(Flight.class, pf.getPfId().getFlightsFlightId()).getFlightId(),
                    em.getReference(Flight.class, pf.getPfId().getFlightsFlightId()).getPlaceFrom(),
                    em.getReference(Flight.class, pf.getPfId().getFlightsFlightId()).getPlaceTo()});
            assignButton(w, pf.getPfId(), fpId, em.getReference(Pilot.class, pf.getPfId().getPilotPilotId()),
                    em.getReference(Flight.class, pf.getPfId().getFlightsFlightId()));
            amountOfRelations++;
        }
    }

    private void createRelation(int w) {
        //making temporary Entities for Relation
        em.getTransaction().begin();
        Pilot pilot = em.getReference(Pilot.class, Integer.parseInt(addRelations.getValueAt(0, 0).toString()));
        Flight flight = em.getReference(Flight.class, Integer.parseInt(addRelations.getValueAt(0, 2).toString()));

        //creating Relation
        pilot.getFlights().add(flight);
        flight.getPilots().add(pilot);
        pfId pfId = new pfId();
        pfId.setPilotPilotId(pilot.getPilotId());
        pfId.setFlightsFlightId(flight.getFlightId());
        fpId fpId = new fpId();
        fpId.setFlightFlightId(flight.getFlightId());
        fpId.setPilotsPilotId(pilot.getPilotId());

        //resetting fields of table
        for (int i = 0; i < 3; i++) {
            addRelations.setValueAt(null, 0, i);
        }

        //adding Relation to table
        Rdtm.addRow(new Object[]{em.getReference(Pilot.class, pfId.getPilotPilotId()).getPilotId(),
                em.getReference(Pilot.class, pfId.getPilotPilotId()).getName(),
                em.getReference(Pilot.class, pfId.getPilotPilotId()).getSurname(), "-",
                em.getReference(Flight.class, pfId.getFlightsFlightId()).getFlightId(),
                em.getReference(Flight.class, pfId.getFlightsFlightId()).getPlaceFrom(),
                em.getReference(Flight.class, pfId.getFlightsFlightId()).getPlaceTo()});
        assignButton(w, pfId, fpId, pilot, flight);
        amountOfRelations++;

        //sending updated Entities to database
        em.persist(pilot);
        em.persist(flight);
        em.getTransaction().commit();
    }

    private void deleteRelation(int w, pfId pf, fpId fp, Pilot p, Flight f) {
        //delete Relation from database
        em.getTransaction().begin();
        em.remove(em.getReference(Pilot_flight.class, pf));
        em.remove(em.getReference(Flight_pilot.class, fp));
        em.getTransaction().commit();
        p.getFlights().remove(f);
        f.getPilots().remove(p);

        //refresh buttons
        refreshButtons(w);
    }

    private void refreshButtons(int w) {
        //remove all buttons
        for (Object o : componentMap.keySet()) {
            if (!(o == null)) {
                jf.remove(getComponentByName(o.toString()));
            }
        }
        jf.repaint();
        //refresh tables
        refreshEntities(w);
        refreshRelations(w);
        createComponentMap();
    }

    private void assignButtons(int w, int id) {

        //button for updating Entity in database
        JButton button1 = new JButton("Upd");
        button1.setName("u" + id);
        button1.setLocation(w / 2 - 140, 80 + amountOfEntities * 16);
        button1.setSize(60, 16);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //determine the position of entity in table
                int rows = lEntities.getRowCount();

                for (int i = 0; i < rows; i++) {
                    if (id == Integer.parseInt(lEntities.getValueAt(i, 0).toString())) {
                        position = i;
                    }
                }

                if (checkEntityValidityOnUpdate(id, position) == 1) {
                    updateEntity(w, id, position);
                }
            }
        });
        //button for deleting Entity from database
        JButton button2 = new JButton("Del");
        button2.setName("d" + id);
        button2.setLocation(w / 2 - 70, 80 + amountOfEntities * 16);
        button2.setSize(60, 16);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteEntity(w, id);
            }
        });

        //adding buttons to JFrame
        jf.add(button1);
        jf.add(button2);
        jf.repaint();
        createComponentMap();
    }

    private void assignButton(int w, pfId pf, fpId fp, Pilot p, Flight f) {
        //button for deleting Relation from database
        JButton button = new JButton("Del");
        button.setName("d" + pf);
        button.setLocation(w - 70, 80 + amountOfRelations * 16);
        button.setSize(60, 16);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRelation(w, pf, fp, p, f);
            }
        });

        //adding button to JFrame
        jf.add(button);
        jf.repaint();
        createComponentMap();
    }

    private int checkEntityValidityOnAdd() {
        //return value, 1 if everything is correct, 0 otherwise
        int result = 1;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<String> passports = new ArrayList<String>();
        ArrayList<Integer> licenses = new ArrayList<Integer>();

        //list of all Entities IDs in database, list of all Entities Pass numbers and Citizenships
        //and list of all Entities License numbers in database
        em.getTransaction().begin();
        List<Pilot> listPilots = em.createQuery(
                "SELECT p FROM Pilot p").getResultList();
        em.getTransaction().commit();
        for (Pilot p : listPilots) {
            ids.add(p.getPilotId());
            passports.add(p.getIdNumber() + p.getCitizenship());
            licenses.add(p.getPilotLicenseNumber());
        }

        //Pilot ID(0)
        //checking if Pilot ID is null or not int
        if ((addEntities.getValueAt(0, 0) == null) || (!(isNumeric(addEntities.getValueAt(0, 0).toString())))) {
            result = 0;
            showMessageDialog(jf, "Pilot ID must be integer!");
            addEntities.setValueAt(null, 0, 0);
        }
        //checking if Pilot ID is longer than 9 characters
        else if (((String) addEntities.getValueAt(0, 0)).length() > 9) {
            result = 0;
            showMessageDialog(jf, "Pilot ID must be at most 9 characters long!");
            addEntities.setValueAt(null, 0, 0);
        }
        //checking if Pilot ID is already in database
        else if (ids.contains(Integer.parseInt(addEntities.getValueAt(0, 0).toString()))) {
            result = 0;
            showMessageDialog(jf, "Pilot ID is already in database!");
            addEntities.setValueAt(null, 0, 0);
        }

        //Name(1)
        //checking if Name is null or not String
        else if ((addEntities.getValueAt(0, 1) == null) || (isNumeric(addEntities.getValueAt(0, 1).toString()))) {
            result = 0;
            showMessageDialog(jf, "Name must be string!");
            addEntities.setValueAt(null, 0, 1);
        }
        //checking if Name is longer than 128 characters
        else if (((String) addEntities.getValueAt(0, 1)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Name must be at most 128 characters long!");
            addEntities.setValueAt(null, 0, 1);
        }

        //Surname(2)
        //checking if Surname is null or not String
        else if ((addEntities.getValueAt(0, 2) == null) || (isNumeric(addEntities.getValueAt(0, 2).toString()))) {
            result = 0;
            showMessageDialog(jf, "Surname must be string!");
            addEntities.setValueAt(null, 0, 2);
        }
        //checking if Surname is longer than 128 characters
        else if (((String) addEntities.getValueAt(0, 2)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Surname must be at most 128 characters long!");
            addEntities.setValueAt(null, 0, 2);
        }

        //Pass number(3)
        //checking if Pass number is longer than 9 characters
        else if (((String) addEntities.getValueAt(0, 3)).length() > 9) {
            result = 0;
            showMessageDialog(jf, "Pass number must be at most 9 characters long!");
            addEntities.setValueAt(null, 0, 3);
        }
        //checking if Pass number is null or not int
        else if ((addEntities.getValueAt(0, 3) == null) || !(isNumeric(addEntities.getValueAt(0, 3).toString()))) {
            result = 0;
            showMessageDialog(jf, "Pass number must be integer!");
            addEntities.setValueAt(null, 0, 3);
        }


        //Citizenship(4)
        //checking if Citizenship is null or not String
        else if ((addEntities.getValueAt(0, 4) == null) || (isNumeric(addEntities.getValueAt(0, 4).toString()))) {
            result = 0;
            showMessageDialog(jf, "Citizenship must be string!");
            addEntities.setValueAt(null, 0, 4);
        }
        //checking if Citizenship is longer than 3 characters
        else if (((String) addEntities.getValueAt(0, 4)).length() > 3) {
            result = 0;
            showMessageDialog(jf, "Citizenship must be at most 3 characters long!");
            addEntities.setValueAt(null, 0, 4);
        }

        //Email(5)
        //checking if Email is null or not String
        else if ((addEntities.getValueAt(0, 5) == null) || (isNumeric(addEntities.getValueAt(0, 5).toString()))) {
            result = 0;
            showMessageDialog(jf, "Email must be string!");
            addEntities.setValueAt(null, 0, 5);
        }
        //checking if Email contains '@' character
        else if (addEntities.getValueAt(0, 5).toString().indexOf('@') == -1) {
            result = 0;
            showMessageDialog(jf, "Email must be in valid form!");
            addEntities.setValueAt(null, 0, 5);
        }
        //checking if Email is longer than 128 characters
        else if (((String) addEntities.getValueAt(0, 5)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Email must be at most 128 characters long!");
            addEntities.setValueAt(null, 0, 5);
        }

        //License number(6)
        //checking if License number is null or not int
        else if ((addEntities.getValueAt(0, 6) == null) || !(isNumeric(addEntities.getValueAt(0, 6).toString()))) {
            result = 0;
            showMessageDialog(jf, "License number must be integer!");
            addEntities.setValueAt(null, 0, 6);
        }
        //checking if License number is longer than 9 characters
        else if (((String) addEntities.getValueAt(0, 6)).length() > 9) {
            result = 0;
            showMessageDialog(jf, "License number must be at most 9 characters long!");
            addEntities.setValueAt(null, 0, 6);
        }
        //checking if License number is already in database
        else if (licenses.contains(Integer.parseInt(addEntities.getValueAt(0, 6).toString()))) {
            result = 0;
            showMessageDialog(jf, "License number is already in database!");
            addEntities.setValueAt(null, 0, 6);
        }

        //checking if passport(combination of Pass number and Citizenship) is already in database
        else if (passports.contains(addEntities.getValueAt(0, 3).toString() + addEntities.getValueAt(0, 4).toString())) {
            result = 0;
            showMessageDialog(jf, "Passport is already in database!");
            addEntities.setValueAt(null, 0, 3);
            addEntities.setValueAt(null, 0, 4);
        }

        return result;
    }

    private int checkEntityValidityOnUpdate(int id, int pos) {
        //return value, 1 if everything is correct, 0 otherwise
        int result = 1;
        Pilot tmp = new Pilot();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<String> passports = new ArrayList<String>();
        ArrayList<Integer> licenses = new ArrayList<Integer>();

        //list of all Entities IDs in database, list of all Entities Pass numbers and Citizenships
        //and list of all Entities License numbers in database
        em.getTransaction().begin();
        tmp = em.getReference(Pilot.class, id);
        List<Pilot> listPilots = em.createQuery(
                "SELECT p FROM Pilot p").getResultList();
        em.getTransaction().commit();
        for (Pilot p : listPilots) {
            ids.add(p.getPilotId());
            passports.add(p.getIdNumber() + p.getCitizenship());
            licenses.add(p.getPilotLicenseNumber());
        }

        //Name(1)
        //checking if Name is null or not String
        if ((lEntities.getValueAt(pos, 1) == null) || (isNumeric(lEntities.getValueAt(pos, 1).toString()))) {
            result = 0;
            showMessageDialog(jf, "Name must be string!");
            lEntities.setValueAt(null, pos, 1);
        }
        //checking if Name is longer than 128 characters
        else if (((String) lEntities.getValueAt(pos, 1)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Name must be at most 128 characters long!");
            lEntities.setValueAt(null, pos, 1);
        }

        //Surname(2)
        //checking if Surname is null or not String
        else if ((lEntities.getValueAt(pos, 2) == null) || (isNumeric(lEntities.getValueAt(pos, 2).toString()))) {
            result = 0;
            showMessageDialog(jf, "Surname must be string!");
            lEntities.setValueAt(null, pos, 2);
        }
        //checking if Surname is longer than 128 characters
        else if (((String) lEntities.getValueAt(pos, 2)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Surname must be at most 128 characters long!");
            lEntities.setValueAt(null, pos, 2);
        }

        //Pass number(3)
        //checking if Pass number is longer than 9 characters
        else if ((lEntities.getValueAt(pos, 3).toString()).length() > 9) {
            result = 0;
            showMessageDialog(jf, "Pass number must be at most 9 characters long!");
            lEntities.setValueAt(null, pos, 3);
        }
        //checking if Pass number is null or not int
        else if ((lEntities.getValueAt(pos, 3) == null) || !(isNumeric(lEntities.getValueAt(pos, 3).toString()))) {
            result = 0;
            showMessageDialog(jf, "Pass number must be integer!");
            lEntities.setValueAt(null, pos, 3);
        }

        //Citizenship(4)
        //checking if Citizenship is null or not String
        else if ((lEntities.getValueAt(pos, 4) == null) || (isNumeric(lEntities.getValueAt(pos, 4).toString()))) {
            result = 0;
            showMessageDialog(jf, "Citizenship must be string!");
            lEntities.setValueAt(null, pos, 4);
        }
        //checking if Citizenship is longer than 3 characters
        else if (((String) lEntities.getValueAt(pos, 4)).length() > 3) {
            result = 0;
            showMessageDialog(jf, "Citizenship must be at most 3 characters long!");
            lEntities.setValueAt(null, pos, 4);
        }

        //Email(5)
        //checking if Email is null or not String
        else if ((lEntities.getValueAt(pos, 5) == null) || (isNumeric(lEntities.getValueAt(pos, 5).toString()))) {
            result = 0;
            showMessageDialog(jf, "Email must be string!");
            lEntities.setValueAt(null, pos, 5);
        }
        //checking if Email contains '@' character
        else if (lEntities.getValueAt(pos, 5).toString().indexOf('@') == -1) {
            result = 0;
            showMessageDialog(jf, "Email must be in valid form!");
            lEntities.setValueAt(null, pos, 5);
        }
        //checking if Email is longer than 128 characters
        else if (((String) lEntities.getValueAt(pos, 5)).length() > 128) {
            result = 0;
            showMessageDialog(jf, "Email must be at most 128 characters long!");
            lEntities.setValueAt(null, pos, 5);
        }

        //License number(6)
        //checking if License number is null or not int
        else if ((lEntities.getValueAt(pos, 6) == null) || !(isNumeric(lEntities.getValueAt(pos, 6).toString()))) {
            result = 0;
            showMessageDialog(jf, "License number must be integer!");
            lEntities.setValueAt(null, pos, 6);
        }
        //checking if License number is longer than 9 characters
        else if ((lEntities.getValueAt(pos, 6).toString()).length() > 9) {
            result = 0;
            showMessageDialog(jf, "License number must be at most 9 characters long!");
            lEntities.setValueAt(null, pos, 6);
        }
        //checking if License number is already in database
        else if (tmp.getPilotLicenseNumber() != Integer.parseInt(lEntities.getValueAt(pos, 6).toString())) {
            System.out.println(tmp.getPilotLicenseNumber());
            System.out.println(tmp.getPilotId());
            System.out.println(pos);
            System.out.println(Integer.parseInt(lEntities.getValueAt(pos, 6).toString()));
            if (licenses.contains(Integer.parseInt(lEntities.getValueAt(pos, 6).toString()))) {
                result = 0;
                showMessageDialog(jf, "License number is already in database!");
                lEntities.setValueAt(null, pos, 6);
            }
        }

        //checking if passport(combination of Pass number and Citizenship) is already in database
        else if (!(passports.contains(tmp.getIdNumber() + tmp.getCitizenship()))) {
            if (passports.contains(lEntities.getValueAt(pos, 3).toString() + lEntities.getValueAt(pos, 4).toString())) {
                result = 0;
                showMessageDialog(jf, "Passport is already in database!");
                lEntities.setValueAt(null, pos, 3);
                lEntities.setValueAt(null, pos, 4);
            }
        }

        return result;
    }

    private int checkRelationValidity() {
        //return value, 1 if everything is correct, 0 otherwise
        int result = 1;
        ArrayList<Integer> pilotIds = new ArrayList<Integer>();
        ArrayList<Integer> flightIds = new ArrayList<Integer>();
        ArrayList<String> relations = new ArrayList<String>();

        //list of all Relations in database, list of all Pilot IDs and Flight IDs in database
        em.getTransaction().begin();
        List<Flight> listFlights = em.createQuery(
                "SELECT f FROM Flight f").getResultList();
        em.getTransaction().commit();
        List<Pilot> listPilots = em.createQuery(
                "SELECT p FROM Pilot p").getResultList();
        for (Pilot p : listPilots) {
            for (Flight f : p.getFlights()) {
                relations.add(p.getPilotId() + "and" + f.getFlightId());
            }
            pilotIds.add(p.getPilotId());
        }
        for (Flight f : listFlights) {
            flightIds.add(f.getFlightId());
        }

        //Pilot ID(0)
        //checking if Pilot ID is null or not int
        if ((addRelations.getValueAt(0, 0) == null) || (!(isNumeric(addRelations.getValueAt(0, 0).toString())))) {
            result = 0;
            showMessageDialog(jf, "Pilot ID must be integer!");
            addRelations.setValueAt(null, 0, 0);
        }
        //checking if Pilot ID is longer than 9 characters
        else if (((String) addRelations.getValueAt(0, 0)).length() > 9) {
            result = 0;
            showMessageDialog(jf, "Pilot ID must be at most 9 characters long!");
            addRelations.setValueAt(null, 0, 0);
        }
        //checking if Pilot ID is not present in database
        else if (!(pilotIds.contains(Integer.parseInt(addRelations.getValueAt(0, 0).toString())))) {
            result = 0;
            showMessageDialog(jf, "Pilot ID is not present in database!");
            addRelations.setValueAt(null, 0, 0);
        }

        //Flight ID(2)
        //checking if Flight ID is null or not int
        else if ((addRelations.getValueAt(0, 2) == null) || (!(isNumeric(addRelations.getValueAt(0, 2).toString())))) {
            result = 0;
            showMessageDialog(jf, "Flight ID must be integer!");
            addRelations.setValueAt(null, 0, 2);
        }
        //checking if Flight ID is longer than 9 characters
        else if (((String) addRelations.getValueAt(0, 2)).length() > 9) {
            result = 0;
            showMessageDialog(jf, "Flight ID must be at most 9 characters long!");
            addRelations.setValueAt(null, 0, 2);
        }
        //checking if Flight ID is not present in database
        else if (!(flightIds.contains(Integer.parseInt(addRelations.getValueAt(0, 2).toString())))) {
            result = 0;
            showMessageDialog(jf, "Flight ID is not present in database!");
            addRelations.setValueAt(null, 0, 2);
        }

        //checking if relation is already in database
        else if (relations.contains(addRelations.getValueAt(0, 0).toString() + "and" + addRelations.getValueAt(0, 2).toString())) {
            result = 0;
            showMessageDialog(jf, "Relation is already in database!");
            addRelations.setValueAt(null, 0, 0);
            addRelations.setValueAt(null, 0, 2);
        }

        return result;
    }
}

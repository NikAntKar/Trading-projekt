package com.example.javafxtest;
import InfoWindow.OpenPos;
import InfoWindow.OpenPosTblView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JsonFixer {

    public JsonFixer()
    {

    }


    public void add(OpenPos openPos)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String fileName = "OpenPositions.json";
        ArrayList<OpenPos> o = new ArrayList<OpenPos>();

            try {
                // Read the existing list of OpenPos objects from the JSON file
                List<OpenPos> openPosList;
                File file = new File(fileName);
                if (file.exists() && file.length()>0) {
                    openPosList = objectMapper.readValue(file, new TypeReference<List<OpenPos>>() {
                    });
                } else {
                    openPosList = new ArrayList<>();
                }
                // Add the new OpenPos object to the list
                openPosList.add(openPos);

                // Write the updated list of OpenPos objects back to the JSON file
                objectMapper.writeValue(file, openPosList);
                System.out.println( openPos.getSymb() +" have been saved to " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public OpenPos getOneItem(String symb)
    {
        String fileName = "OpenPositions.json";
        OpenPos holdOpenPos = new OpenPos();
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            List<OpenPos> o = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {});

            for(int i=0; i<o.size(); i++)
            {
                if(o.get(i).getSymb().equals(symb)) {
                    holdOpenPos = o.get(i);
                    return holdOpenPos;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return holdOpenPos;

    }
    public void read()
    {
        String fileName = "OpenPositions.json";

        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            List<OpenPos> o = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<OpenPos> addToList() {
        boolean fileIsEmpty = false;
        String fileName = "OpenPositions.json";
        ArrayList<OpenPos> o = new ArrayList<OpenPos>();


        if (isJsonFileEmpty(fileName)) {
            System.out.println("JSON file is empty.");
            fileIsEmpty = true;
        }

        if(!fileIsEmpty) {

            try {
                // Create an ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();
                o = objectMapper.readValue(new File(fileName), new TypeReference<ArrayList<OpenPos>>() {
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    public void update(OpenPos openPos)
    {
        String fileName = "OpenPositions.json";
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            String symb = openPos.getSymb();
            List<OpenPos> o = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {});
            // Read data from the JSON file into an object
            for(int i =0; i<o.size();i++)
            {
                if(o.get(i).getSymb().equals(symb))
                {
                    o.set(i, openPos);
                }
            }
            objectMapper.writeValue(new File(fileName), o);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean isJsonFileEmpty(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
            return true; // Handle the exception appropriately in your code
        }
    }
    public void loadToView(TableView<OpenPos> tblOpenPos)
    {
        boolean fileIsEmpty = false;
        String fileName = "OpenPositions.json";
        if (isJsonFileEmpty(fileName)) {
            System.out.println("JSON file is empty.");
            fileIsEmpty = true;
        }

        if(!fileIsEmpty) {
            try {
                // Create an ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();
                List<OpenPos> openPosList = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {
                });
                ObservableList<OpenPos> items = tblOpenPos.getItems();

                items.addAll(openPosList);
                tblOpenPos.refresh();
                // Access other properties as needed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void remove(TableView tblOpen)
    {
        OpenPos o = (OpenPos) tblOpen.getSelectionModel().getSelectedItem();
        if(o == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected item");
            alert.setContentText("No item are selected");
            alert.setHeaderText(null); // Optional header text
            alert.showAndWait();        }
        else
        {
        int removeId= o.getId();
        int size = tblOpen.getItems().size();
        String fileName = "OpenPositions.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<OpenPos> openPosList = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {
            });
            List<OpenPos> updatedList = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (openPosList.get(i).getId() == removeId) {
                    // Add the item to the updated list if its symbol doesn't match the one to delete
                    System.out.println(o.getSymb() + " have been removed from the file");
                    index = i;
                } else {
                    updatedList.add(openPosList.get(i));
                }
            }
            objectMapper.writeValue(new File(fileName), updatedList);
            tblOpen.getItems().remove(index);
            tblOpen.refresh();

            // Access other properties as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
    public void remove (OpenPos openPos)
    {
        int removeId= openPos.getId();
        String fileName = "OpenPositions.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<OpenPos> openPosList = objectMapper.readValue(new File(fileName), new TypeReference<List<OpenPos>>() {
            });
            int size = openPosList.size();
            List<OpenPos> updatedList = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < size; i++) {
                if (openPosList.get(i).getId() == removeId) {
                    // Add the item to the updated list if its symbol doesn't match the one to delete
                    System.out.println(openPos.getSymb() + " have been removed from the file");
                    index = i;
                } else {
                    updatedList.add(openPosList.get(i));
                }
            }
            objectMapper.writeValue(new File(fileName), updatedList);
            }
         catch (IOException e) {
            e.printStackTrace();
        }
    }
}



//    public void add(OpenPos openPos) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//
//        String fileName = "OpenPositions.json";
//
//        try {
//            File file = new File(fileName);
//
//            // Check if the file exists
//            if (!file.exists()) {
//                // If the file doesn't exist, create an empty JSON array
//                objectMapper.writeValue(file, new ArrayList<>());
//            }
//
//            // Read the existing JSON array from the file
//            List<OpenPos> existingList = objectMapper.readValue(file, new TypeReference<List<OpenPos>>() {});
//
//            // Add the new OpenPos object to the existing list
//            existingList.add(openPos);
//
//            // Write the updated list back to the file
//            objectMapper.writeValue(file, existingList);
//            System.out.println("OpenPos object added to " + fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




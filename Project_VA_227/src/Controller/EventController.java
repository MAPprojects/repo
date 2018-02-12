package Controller;

import Domain.Event;
import Domain.ExceptionValidator;
import Domain.Tip;
import Domain.User;
import Service.EventService;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventController  implements Initializable{

    public Pagination paginator2;
    public Pagination paginator1;
    public ComboBox<String> actions;
    public Label label;
    public Button applyBtn;
    public TextField name;
    public TextField detalii;
    public DatePicker evdate;
    public DatePicker deadlinedate;

    User user;
    EventService eventService;
    List<Event> myEvents, otherEventsList;

    void setEnviroment(EventService eventService, User user)
    {
        this.eventService = eventService;
        this.user = user;
        reloadData();
        if (user.getTip() == Tip.USER)
        {
            label.setVisible(false);
            applyBtn.setVisible(false);
            actions.setVisible(false);
        }
        else
            actions.getItems().addAll("Add", "Update", "Remove");
        load();
    }

    private void reloadData()
    {
        myEvents = this.eventService.filterUser(this.user.getID());
        otherEventsList = StreamSupport.stream(this.eventService.getAll().spliterator(), false)
                .map(this.eventService::find)
                .filter(x -> !myEvents.contains(x))
                .collect(Collectors.toList());
    }

    private void load_1()
    {
        paginator1.setCurrentPageIndex(0);
        paginator1.setMaxPageIndicatorCount(myEvents.size() <= 3 ? 1 : (myEvents.size() - 1)/ 3 + 1);
        paginator1.setPageCount(myEvents.size() <= 3 ? 1 : (myEvents.size() - 1)/ 3 + 1);
        paginator1.setPageFactory(index -> createEventPage(index, myEvents));
    }

    private void load()
    {
        load_1();
        load_2();
    }

    private void load_2() {
        paginator2.setCurrentPageIndex(0);
        paginator2.setMaxPageIndicatorCount(otherEventsList.size() <= 3 ? 1 : (otherEventsList.size() - 1)/ 3 + 1);
        paginator2.setPageCount(otherEventsList.size() <= 3 ? 1 : (otherEventsList.size() - 1)/ 3 + 1);
        paginator2.setPageFactory(index -> createEventPage(index, otherEventsList));
    }

    private Node createEventPage(Integer pageNumber, List<Event> list) {
        AtomicReference<Double> start = new AtomicReference<>( 10.0);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinSize(paginator1.getMinWidth(), paginator2.getMinHeight());
        int fromIndex = pageNumber * 3;
        int toIndex = Math.min(fromIndex + 3, list.size());
        addEvents(list.subList(fromIndex, toIndex), start, anchorPane.getChildren());
        return anchorPane;
    }

    private void addEvents(List<Event> myEvents, AtomicReference<Double> start, ObservableList<Node> children) {
        myEvents.forEach(event -> {
            Pane eventPane = getEventPane(event);
            AnchorPane.setLeftAnchor(eventPane, start.get());
            AnchorPane.setTopAnchor(eventPane, 10.0);
            children.add(eventPane);
            start.updateAndGet(v -> (v + 215.0));
        });
    }

    private Text getText(String text, double yLayout, int fontSize)
    {
        Text Text = new Text(text);
        Text.setStyle(String.format("-fx-font-size: %d", fontSize));
        Text.applyCss();
        double width = Text.getBoundsInLocal().getWidth();
        Text.setLayoutX(width < 230 ? 114 - (width / 2.0) : 0);
        Text.setLayoutY(yLayout);
        return Text;
    }

    private Pane getEventPane(Event event)
    {
        Pane eventPane = new Pane();
        eventPane.resize(230, 235);

        eventPane.getChildren().add(getText(event.getName(), 20, 16));
        eventPane.getChildren().add(getText("Event Date", 50, 15));
        eventPane.getChildren().add(getText(event.getEventDate().toString(), 70, 14));
        eventPane.getChildren().add(getText("Deadline", 100, 15));
        eventPane.getChildren().add(getText(event.getEventDate().toString(), 120, 14));

        Button subscribe = new Button("Subscribe");
        subscribe.setLayoutX(60);
        subscribe.setLayoutY(170);
        subscribe.setOnMouseClicked(mouseEvent -> {
            try {
                eventService.addUserToEvent(user.getID(), event.getId());
                myEvents.add(event);
                otherEventsList.remove(event);
                load();
            } catch (ExceptionValidator exceptionValidator) {
                AbstractController.showError(exceptionValidator.getMessage());
            }
        });
        if (otherEventsList.contains(event))
            eventPane.getChildren().add(subscribe);

        Tooltip details = TooltipBuilder.create().text(event.getDetails()).prefWidth(150).wrapText(true).build();
        if (event.getDetails() != null && event.getDetails().length() > 0)
            Tooltip.install(eventPane, details);
        eventPane.setStyle("-fx-background-color: CORNFLOWERBLUE; -fx-min-width: 200px; -fx-min-height: 235px;" +
                "-fx-border-radius: 25; -fx-background-radius: 25;");
        eventPane.setOnMouseClicked(event1 -> selectEvent(event));
        return eventPane;
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void selectEvent(Event event)
    {
        selectedEvent = event;
        name.setText(event.getName());
        detalii.setText(event.getDetails());
        evdate.setValue(LocalDate.parse(event.getEventDate().toString(), formatter));
        deadlinedate.setValue(LocalDate.parse(event.getDeadline().toString(), formatter));
    }

    private void clearDetalis()
    {
        selectedEvent = null;
        name.clear();
        detalii.clear();
        evdate.setValue(null);
        deadlinedate.setValue(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private Event selectedEvent = null;
    
    public void handleApply() {
        if (selectedEvent == null && actions.getSelectionModel().getSelectedIndex() > 0)
            AbstractController.showError("Please select an event");
        else if (actions.getSelectionModel().getSelectedIndex() < 0)
            AbstractController.showError("Please select an action");
        else
        {
            switch (actions.getSelectionModel().getSelectedItem())
            {
                case "Add" :
                    try {
                        eventService.add(new Event(-1, name.getText(), java.sql.Date.valueOf(evdate.getValue()),
                                java.sql.Date.valueOf(deadlinedate.getValue()), detalii.getText(), new ArrayList<>()));
                        reloadData();
                        load_2();
                        clearDetalis();
                    } catch (Exception exceptionValidator) {
                        AbstractController.showError(exceptionValidator.getMessage());
                    }
                    break;
                case "Update" :
                    try {
                        eventService.update(new Event(selectedEvent.getId(), name.getText(), java.sql.Date.valueOf(evdate.getValue()),
                                java.sql.Date.valueOf(deadlinedate.getValue()), detalii.getText(), selectedEvent.getUserIds()));
                        reloadData();
                        if (selectedEvent.getUserIds().contains(user.getID()))
                            load_1();
                        else
                            load_2();
                        clearDetalis();
                    } catch (Exception exceptionValidator) {
                        AbstractController.showError(exceptionValidator.getMessage());
                    }
                    break;
                case "Remove" :
                    try {
                        eventService.remove(selectedEvent.getId());
                        reloadData();
                        if (selectedEvent.getUserIds().contains(user.getID()))
                            load_1();
                        else
                            load_2();
                        clearDetalis();
                    } catch (Exception exceptionValidator) {
                        AbstractController.showError(exceptionValidator.getMessage());
                    }
                    break;
            }
        }
    }
}

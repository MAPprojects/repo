package View;

import Utils.HyperlinkObserver;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Border;

public class ViewGradesCell<T> extends TableCell<T, Void> {
    private final Hyperlink link;
    private HyperlinkObserver hyperlinkObserver;

    public ViewGradesCell(String text, HyperlinkObserver hyperlinkObserver) {
        link = new Hyperlink(text);
        link.setBorder(Border.EMPTY);
        link.setPadding(new Insets(0, 0, 0, 0));
        this.hyperlinkObserver = hyperlinkObserver;
        link.setOnAction(event -> {
            hyperlinkObserver.onHyperlinkClick(getTableRow().getIndex());
            //getTableView().getItems().remove(getTableRow().getIndex());
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : link);
    }
}

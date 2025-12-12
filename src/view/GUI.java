package view;

import exceptions.ADTException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Window;
import model.prg.PrgState;
import model.prg.adt.ExeStack;
import model.stmts.IStmt;
import model.values.IValue;
import controller.Controller;
import view.tree.TreeCanvas;
import view.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GUI extends Application {

    public record HeapEntry(Integer address, String value) {
    }

    public record SymTableEntry(String varName, String value) {
    }

    private Controller controller;
    private TextField prgStateCountField;
    private TableView<HeapEntry> heapTable;
    private ListView<String> outList;
    private ListView<String> fileTableList;
    private ListView<Integer> prgStateIdList;
    private TableView<SymTableEntry> symTable;
    private ListView<String> exeStackList;
    private HBox treesContainer;
    private Map<Integer, TreeCanvas> programTrees;
    private Map<Integer, TreeNode> treeRoots;
    private Map<Integer, TreeNode> currentNodes;

    static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("images/Larva.png"))
                );
                java.awt.Taskbar.getTaskbar().setIconImage(
                        javafx.embed.swing.SwingFXUtils.fromFXImage(icon, null)
                );
            } catch (Exception e) {
                System.err.println("Could not set dock icon: " + e.getMessage());
            }
        }
        showProgramSelectionWindow(primaryStage);
    }

    private void showProgramSelectionWindow(Stage primaryStage) {
        Stage selectionStage = new Stage();
        selectionStage.setTitle("Larva - Select program");

        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("images/Larva.png"))
            );
            selectionStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        ListView<String> programListView = new ListView<>();
        ObservableList<String> programs = FXCollections.observableArrayList();

        Interpreter interpreter = new Interpreter();
        ArrayList<IStmt> examplePrograms = interpreter.getPrgL();
        ArrayList<Controller> controllerList = interpreter.getClist();
        for (int i = 0; i < examplePrograms.size(); i++) {
            String description;
            description = (i + 1) + ": " + examplePrograms.get(i).toString();
            programs.add(description);
        }

        programListView.setItems(programs);
        programListView.setPrefHeight(600);
        programListView.setStyle("-fx-font-size: 17px; font-family: \"gf_Montserrat variant1\", Tofu; font-weight: 500; font-style: italic;");

        Button selectButton = new Button("Select Program");
        selectButton.setPrefWidth(150);
        selectButton.setOnAction(_ -> {
            int index = programListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Select Log File");
                fileChooser.getExtensionFilters().add(
                        new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt")
                );
                fileChooser.setInitialFileName("logs.txt");

                java.io.File selectedFile = fileChooser.showSaveDialog(selectionStage);
                if (selectedFile != null) {
                    try {
                        controllerList.get(index).getRepo().setLogFilePath(selectedFile.getAbsolutePath());
                        selectionStage.close();
                        openMainWindow(primaryStage, controllerList.get(index));
                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "Error setting log file: " + e.getMessage());
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a program!");
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(15));
        Label titleLabel = new Label("Select a program to execute:");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-alignment: top-center");
        layout.getChildren().addAll(titleLabel, programListView, selectButton);
        layout.setAlignment(javafx.geometry.Pos.CENTER);
        Scene scene = new Scene(layout, 1200, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleSheet.css")).toExternalForm());
        selectionStage.setScene(scene);
        selectionStage.show();
    }

    private void openMainWindow(Stage stage, Controller program) {
        try {
            controller = program;

            stage.setTitle("Larva - Program Dashboard");
            try {
                javafx.scene.image.Image icon = new javafx.scene.image.Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("images/Larva.png"))
                );
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Could not load application icon: " + e.getMessage());
            }

            initializeComponents();

            BorderPane mainLayout = new BorderPane();
            mainLayout.setPadding(new Insets(10));

            HBox topBar = new HBox(10);
            topBar.setPadding(new Insets(5));
            Label countLabel = new Label("Number of PrgStates:");
            countLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
            topBar.getChildren().addAll(countLabel, prgStateCountField);
            mainLayout.setTop(topBar);

            HBox centerLayout = new HBox(15);
            centerLayout.setPadding(new Insets(10, 20, 10, 20));

            VBox leftColumn = new VBox(10);
            leftColumn.setPrefWidth(308);
            Label heapLabel = new Label("Heap Table:");
            heapLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            leftColumn.getChildren().addAll(heapLabel, heapTable);

            Label outLabel = new Label("Output:");
            outLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            leftColumn.getChildren().addAll(outLabel, outList);

            VBox middleColumn = new VBox(10);
            middleColumn.setPrefWidth(250);
            Label fileLabel = new Label("File Table:");
            fileLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            middleColumn.getChildren().addAll(fileLabel, fileTableList);

            Label idsLabel = new Label("PrgState IDs:");
            idsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            middleColumn.getChildren().addAll(idsLabel, prgStateIdList);

            VBox rightColumn = new VBox(10);
            rightColumn.setPrefWidth(308);
            Label symTableLabel = new Label("Symbol Table:");
            symTableLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            rightColumn.getChildren().addAll(symTableLabel, symTable);

            Label stackLabel = new Label("Execution Stack:");
            stackLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
            rightColumn.getChildren().addAll(stackLabel, exeStackList);

            HBox leftSide = new HBox(15);
            leftSide.setPadding(new Insets(10, 10, 10, 20));
            leftSide.getChildren().addAll(leftColumn, middleColumn, rightColumn);

            VBox treePlayground = new VBox(10);
            treePlayground.setPrefWidth(800);
            Label playgroundLabel = new Label("Execution Trees Playground:");
            playgroundLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");

            treesContainer = new HBox(15);
            treesContainer.setPadding(new Insets(10));

            ScrollPane treeScrollPane = new ScrollPane(treesContainer);
            treeScrollPane.setFitToHeight(true);
            treeScrollPane.setFitToWidth(true);
            treeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            treeScrollPane.setPrefViewportHeight(650);
            treeScrollPane.setPannable(true);

            treePlayground.getChildren().addAll(playgroundLabel, treeScrollPane);

            SplitPane splitPane = new SplitPane();
            splitPane.getItems().addAll(leftSide, treePlayground);
            splitPane.setDividerPositions(0.57);
            splitPane.getStyleClass().add("invisible-divider");

            HBox.setHgrow(leftSide, Priority.ALWAYS);
            HBox.setHgrow(treePlayground, Priority.ALWAYS);

            mainLayout.setCenter(splitPane);

            Button runOneStepButton = new Button("Run One Step");
            runOneStepButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
            runOneStepButton.setOnAction(_ -> executeOneStep());

            HBox bottomBar = new HBox(runOneStepButton);
            bottomBar.setPadding(new Insets(10));
            bottomBar.setAlignment(javafx.geometry.Pos.CENTER);
            mainLayout.setBottom(bottomBar);

            prgStateIdList.getSelectionModel().selectedItemProperty().addListener(
                    (_, _, newVal) -> {
                        if (newVal != null) {
                            updateSelectedPrgStateInfo(newVal);
                        }
                    }
            );
            Scene scene = new Scene(mainLayout, 2400, 850);

            treePlayground.prefWidthProperty().bind(
                    scene.widthProperty()
                            .subtract(leftColumn.widthProperty())
                            .subtract(middleColumn.widthProperty())
                            .subtract(rightColumn.widthProperty())
                            .subtract(108)
            );

            treesContainer.prefWidthProperty().bind(treePlayground.widthProperty());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/styleSheet.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
            updateUI();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error initializing program " + e.getMessage());
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("styles/styleSheet.css")).toExternalForm()
            );
            try {
                javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(
                        new javafx.scene.image.Image(
                                Objects.requireNonNull(getClass().getResourceAsStream("images/Larva.png"))
                        )
                );
                icon.setFitHeight(48);
                icon.setFitWidth(48);
                alert.setGraphic(icon);
            } catch (Exception e2) {
                System.err.println("Could not load alert icon: " + e2.getMessage());
            }
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        programTrees = new HashMap<>();
        treeRoots = new HashMap<>();
        currentNodes = new HashMap<>();

        prgStateCountField = new TextField();
        prgStateCountField.setEditable(false);
        prgStateCountField.setPrefWidth(50);

        heapTable = new TableView<>();
        heapTable.setPrefHeight(305);
        TableColumn<HeapEntry, Number> addressCol = new TableColumn<>("Address");
        addressCol.setStyle("-fx-font-size: 16; -fx-border-radius: 7;");
        addressCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().address()));
        addressCol.setPrefWidth(100);

        TableColumn<HeapEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setStyle("-fx-font-size: 16; -fx-border-radius: 7;");
        valueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().value()));
        valueCol.setPrefWidth(204);

        heapTable.getColumns().addAll(addressCol, valueCol);

        outList = new ListView<>();
        outList.setPrefHeight(305);

        fileTableList = new ListView<>();
        fileTableList.setPrefHeight(305);
        fileTableList.setStyle("-fx-font-size: 16;");

        prgStateIdList = new ListView<>();
        prgStateIdList.setPrefHeight(305);
        prgStateIdList.setStyle("-fx-font-size: 16;");

        symTable = new TableView<>();
        symTable.setPrefHeight(305);
        TableColumn<SymTableEntry, String> varNameCol = new TableColumn<>("Variable");
        varNameCol.setStyle("-fx-font-size: 16; -fx-border-radius: 7;");
        varNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().varName()));
        varNameCol.setPrefWidth(100);

        TableColumn<SymTableEntry, String> varValueCol = new TableColumn<>("Value");
        varValueCol.setStyle("-fx-font-size: 16; -fx-border-radius: 7;");
        varValueCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().value()));
        varValueCol.setPrefWidth(204);

        symTable.getColumns().addAll(varNameCol, varValueCol);

        exeStackList = new ListView<>();
        exeStackList.setPrefHeight(305);
    }

    private void createTreeForProgram(int programId) {
        VBox treeBox = new VBox(10);
        treeBox.setPrefWidth(800);

        Label label = new Label("Program State ID: " + programId);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TreeNode root = new TreeNode("Execution Tree [ID: " + programId + "]");
        TreeCanvas canvas = new TreeCanvas(10, 10);
        canvas.setRoot(root);

        Group zoomGroup = new Group(canvas);
        ScrollPane canvasScroll = new ScrollPane(zoomGroup);

        canvas.setOwnerScrollPane(canvasScroll);
        canvas.setZoomGroup(zoomGroup);

        final double zoomFactorStep = 1.1;
        zoomGroup.setOnScroll(event -> {
            if (!event.isControlDown()) return;
            event.consume();
            double scale = zoomGroup.getScaleX();
            scale = event.getDeltaY() > 0 ? scale * zoomFactorStep : scale / zoomFactorStep;
            scale = Math.max(0.2, Math.min(scale, 5.0));
            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);
        });

        HBox zoomControls = new HBox(5);
        Button zoomInBtn = new Button("+");
        Button zoomOutBtn = new Button("-");
        Button resetZoomBtn = new Button("100%");
        Button exportBtn = new Button("Export PNG");
        zoomInBtn.setOnAction(_ -> {
            double scale = Math.min(zoomGroup.getScaleX() * zoomFactorStep, 5.0);
            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);
        });

        zoomOutBtn.setOnAction(_ -> {
            double scale = Math.max(zoomGroup.getScaleX() / zoomFactorStep, 0.2);
            zoomGroup.setScaleX(scale);
            zoomGroup.setScaleY(scale);
        });

        resetZoomBtn.setOnAction(_ -> {
            zoomGroup.setScaleX(1.0);
            zoomGroup.setScaleY(1.0);
        });

        exportBtn.setOnAction(_ -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Export execution tree");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PNG Image", "*.png")
            );
            fc.setInitialFileName("tree-" + programId + ".png");

            Window window = treeBox.getScene() != null ? treeBox.getScene().getWindow() : null;
            File file = fc.showSaveDialog(window);
            if (file != null) {
                try {
                    canvas.exportToPng(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        zoomControls.getChildren().addAll(
                new Label("Zoom:"),
                zoomOutBtn,
                zoomInBtn,
                resetZoomBtn,
                exportBtn
        );

        treeBox.getChildren().addAll(label, zoomControls, canvasScroll);
        treesContainer.getChildren().add(treeBox);

        programTrees.put(programId, canvas);
        treeRoots.put(programId, root);
        currentNodes.put(programId, root);
    }

    private void updateTreeForProgram(int programId, IStmt stmt) {
        if (!treeRoots.containsKey(programId)) {
            createTreeForProgram(programId);
        }

        TreeNode currentNode = currentNodes.get(programId);
        String stmtStr = stmt.toString();
        TreeNode newNode;

        if (stmt instanceof model.stmts.CompStmt) {
            newNode = new TreeNode(stmtStr);
            currentNode.setRight(newNode);
            currentNodes.put(programId, newNode);
        } else if (stmt instanceof model.stmts.IfStmt) {
            newNode = new TreeNode(stmtStr);
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(newNode);
            } else {
                currentNode.setRight(newNode);
                currentNodes.put(programId, currentNode.getRight());
            }
        } else {
            newNode = new TreeNode(stmtStr);
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(newNode);
            } else {
                currentNode.setRight(newNode);
                currentNodes.put(programId, currentNode.getRight());
            }
        }

        TreeCanvas canvas = programTrees.get(programId);
        canvas.redraw();

        TreeNode targetNode = newNode;
        Platform.runLater(() -> canvas.scrollToNode(targetNode));
    }

    private void updateUI() {
        List<PrgState> prgStates = controller.getRepo().getPrgList();

        prgStateCountField.setText(String.valueOf(prgStates.size()));

        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        if (!prgStates.isEmpty()) {
            Map<Integer, IValue> heap = prgStates.getFirst().getHeap().getContent();
            for (Map.Entry<Integer, IValue> entry : heap.entrySet()) {
                heapEntries.add(new HeapEntry(entry.getKey(), entry.getValue().toString()));
            }
        }
        heapTable.setItems(heapEntries);

        ObservableList<String> outItems = FXCollections.observableArrayList();
        if (!prgStates.isEmpty()) {
            outItems.addAll(prgStates.getFirst().getOut().getList().stream()
                    .toList());
        }
        outList.setItems(outItems);

        ObservableList<String> fileItems = FXCollections.observableArrayList();
        if (!prgStates.isEmpty()) {
            fileItems.addAll(String.valueOf(prgStates.getFirst().getFileTable().getContent().keySet()));
        }
        fileTableList.setItems(fileItems);

        ObservableList<Integer> ids = FXCollections.observableArrayList();
        ids.addAll(prgStates.stream().map(PrgState::getId).toList());
        prgStateIdList.setItems(ids);

        if (!ids.isEmpty() && prgStateIdList.getSelectionModel().getSelectedItem() == null) {
            prgStateIdList.getSelectionModel().select(0);
        } else if (!ids.isEmpty()) {
            Integer currentSelection = prgStateIdList.getSelectionModel().getSelectedItem();
            if (ids.contains(currentSelection)) {
                updateSelectedPrgStateInfo(currentSelection);
            } else {
                prgStateIdList.getSelectionModel().select(0);
            }
        }
        for (PrgState p : prgStates) {
            if (!treeRoots.containsKey(p.getId())) {
                createTreeForProgram(p.getId());
            }
        }
    }

    private void updateSelectedPrgStateInfo(Integer id) {
        List<PrgState> prgStates = controller.getRepo().getPrgList();
        Optional<PrgState> selectedState = prgStates.stream()
                .filter(state -> state.getId() == id)
                .findFirst();

        if (selectedState.isPresent()) {
            PrgState state = selectedState.get();

            ObservableList<SymTableEntry> symEntries = FXCollections.observableArrayList();
            Map<String, IValue> symTableContent = state.getSymTable().getContent();
            for (Map.Entry<String, IValue> entry : symTableContent.entrySet()) {
                symEntries.add(new SymTableEntry(entry.getKey(), entry.getValue().toString()));
            }
            symTable.setItems(symEntries);

            ObservableList<String> stackItems = FXCollections.observableArrayList();
            List<IStmt> stackContent = ((ExeStack<IStmt>) state.getExeStack()).getReverse().reversed();
            stackItems.addAll(stackContent.stream()
                    .map(IStmt::toString)
                    .toList());
            exeStackList.setItems(stackItems);
        }
    }

    private void executeOneStep() {
        List<PrgState> prgList = controller.removeCompletedPrg(controller.getRepo().getPrgList());
        if (prgList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No more programs left to run! ");
            alert.setHeaderText("Execution has been completed!");
            alert.setTitle("Execution information");
            alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("styles/styleSheet.css")).toExternalForm()
            );
            alert.getDialogPane().setStyle("-fx-font-size: 16");
            try {
                javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(
                        new javafx.scene.image.Image(
                                Objects.requireNonNull(getClass().getResourceAsStream("images/Larva.png"))
                        )
                );
                icon.setFitHeight(48);
                icon.setFitWidth(48);
                alert.setGraphic(icon);
            } catch (Exception e) {
                System.err.println("Could not load alert icon: " + e.getMessage());
            }
            alert.showAndWait();
            return;
        }
        for (PrgState state : prgList) {
            if (!state.getExeStack().isEmpty()) {
                try {
                    IStmt nextStmt = state.getExeStack().top();
                    updateTreeForProgram(state.getId(), nextStmt);
                } catch (ADTException e) {
                    System.err.println("Error getting statement: " + e.getMessage());
                }
            }
        }
        controller.oneStepForAllPrg(prgList);
        updateUI();
    }
}

/*
Cristian Daniel Neacsu 
Student Number: 964379
Declaration: This is my own work.
*/

package photoshop;//to be removed if necessary
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java .math.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.stage.Window;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
public class Photoshop extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
		stage.setTitle("Photoshop");
                
		//Read the image
                //if you change the image to the cytrus one , the UI will be a bit messy
                //but still work properly.
		Image image = new Image(new FileInputStream("raytrace.jpg"));  

		//Create the graphical view of the image
		ImageView imageView = new ImageView(image); 
		
		//Create the simple GUI
		Button invert_button = new Button("Invert");
		Button gamma_button = new Button("Gamma Correct");
		Button contrast_button = new Button("Contrast Stretching Interface");
                Button contrast_button1 = new Button("Apply Contrast Stretching");
		Button histogram_button_red = new Button("Histogram Red");
                Button histogram_button_green = new Button("Histogram Green");
                Button histogram_button_blue = new Button("Histogram Blue");
                Button histogram_button_grey = new Button("Histogram Grey");
                Button histogram_button_equal = new Button("Histogram Equalization");
		Button cc_button = new Button("Cross Correlation");
                Button reset = new Button("reset image");
                TextField gammaT=new TextField("gamma");
                TextField s1T=new TextField("s1");
                TextField s2T=new TextField("s2");
                TextField r1T=new TextField("r1");
                TextField r2T=new TextField("r2");
        
               

		//Add all the event handlers (this is a minimal GUI - you may try to do better)
		invert_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Invert");
				//At this point, "image" will be the original image
				//imageView is the graphical representation of an image
				//imageView.getImage() is the currently displayed image
				
				//Let's invert the currently displayed image by calling the invert function later in the code
				Image inverted_image=ImageInverter(imageView.getImage());
				//Update the GUI so the new image is displayed
				imageView.setImage(inverted_image);
            }
        });      
                //Question 1. Please Input a gamma value in the texfield "gamma" then press the button "Gamma Correct".
		gamma_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Gamma Correction");
                
                double gamma=Float.parseFloat(gammaT.getCharacters().toString());
                double [] lookTable=new double [256];
                
                for(int y = 0; y < 256; y++) {
                        lookTable[y]=((Math.pow(((double)y)/255,1.0/gamma)));
                }
                //Image gamma_image=ImageGC(imageView.getImage(),gamma);
                Image gamma_image=ImageGC2(imageView.getImage(),gamma,lookTable);
		imageView.setImage(gamma_image);
            }
        });
                //Question 2. Please Input values for s1,s2,r1,r2, then press then button "Contrast Stretching Interface.
                //The red circle is for s1,r1 while the blue on is for s2,r2. You can drag them in the interface and the values will update on main app window.
                //When you have decided the input values press the button "Apply Contrast Stretching".
                //You also have a button "reset image" that resets the image to default.
		contrast_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                           
                double s1=Double.parseDouble(s1T.getCharacters().toString());
                double s2=Double.parseDouble(s2T.getCharacters().toString());
                double r1=Double.parseDouble(r1T.getCharacters().toString());
                double r2=Double.parseDouble(r2T.getCharacters().toString());
           
                Stage window = new Stage();
                Group root=new Group();                        
                Scene s = new Scene(root, 255, 255, Color.WHITE);
                Canvas contrast_graph=new Canvas(255,255); //r1=50 s1=20 r2=200 s2=225
                GraphicsContext gc = contrast_graph.getGraphicsContext2D(); // top-left=0.0 ; bot-left=0.255 ; bot-right=255.255 ; top-right=255.0 
                Line line01=new Line();
                Line line12=new Line();
                Line line20=new Line();
                Text dot1 = new Text(r1, 255-s1, "O");
                dot1.setFill(Color.RED);
                dot1.setScaleX(1.0);
                dot1.setScaleY(1.0);                                
                Text dot2 = new Text(r2, 255-s2, "O");
                dot2.setFill(Color.BLUE);
                dot2.setScaleX(1.0);
                dot2.setScaleY(1.0);                

                dot1.setOnDragDetected(new EventHandler <MouseEvent>() {
                    public void handle(MouseEvent event) {
                        // drag was detected, start drag-and-drop gesture
                        System.out.println("onDragDetected");
                        // allow any transfer mode 
                        Dragboard db = dot1.startDragAndDrop(TransferMode.ANY);
                        event.setDragDetect(true);
                        // put a string on dragboard 
                        ClipboardContent content = new ClipboardContent();
                        content.putString(dot1.getText());
                        db.setContent(content);
                        event.consume();
                    }
                });
                
                dot2.setOnDragDetected(new EventHandler <MouseEvent>() {
                    public void handle(MouseEvent event) {
                        // drag was detected, start drag-and-drop gesture
                        System.out.println("onDragDetected");
                        //s.setCursor(Cursor.NONE);
                        // allow any transfer mode 
                        Dragboard db = dot2.startDragAndDrop(TransferMode.ANY);
                        event.setDragDetect(true);
                        // put a string on dragboard 
                        ClipboardContent content = new ClipboardContent();
                        content.putString(dot1.getText());
                        db.setContent(content);
                        event.consume();
                    }
                });

                contrast_graph.setOnDragOver(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                       
                        if(event.getGestureSource()==dot1){
                        dot1.setX(event.getSceneX());
                        dot1.setY(event.getSceneY());                        
                        line01.setStartX(0); 
                        line01.setStartY(255); 
                        line01.setEndX(event.getSceneX()); 
                        line01.setEndY(event.getSceneY());                         
                        line12.setStartX(event.getSceneX()); 
                        line12.setStartY(event.getSceneY()); 
                        line12.setEndX(dot2.getX()); 
                        line12.setEndY(dot2.getY());          
                        }
                        else
                            {
                        dot2.setX(event.getSceneX());
                        dot2.setY(event.getSceneY());                        
                        line20.setStartX(event.getSceneX()); 
                        line20.setStartY(event.getSceneY()); 
                        line20.setEndX(255); 
                        line20.setEndY(0);                         
                        line12.setEndX(event.getSceneX()); 
                        line12.setEndY(event.getSceneY()); 
                        line12.setStartX(dot1.getX()); 
                        line12.setStartY(dot1.getY()); 

                        }
                        
                        final double r1=dot1.getX()/255;
                        final double r2=dot2.getX()/255;
                        final double s1=(255-dot1.getY())/255;
                        final double s2=(255-dot2.getY())/255;
                        r1T.setText(String.valueOf(r1*255));
                        s1T.setText(String.valueOf(s1*255));
                        r2T.setText(String.valueOf(r2*255));
                        s2T.setText(String.valueOf(s2*255));                       
                        
                        event.acceptTransferModes(TransferMode.MOVE);
                        event.consume();
                    }
                });                
                
                contrast_graph.setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {   
                        System.out.print("Gata1");
                        event.setDropCompleted(true);
                        event.consume();
                    }
                });       
                        root.getChildren().addAll(contrast_graph,dot1,dot2,line01,line12,line20);
                        window.setScene(s);
                        window.show();
            }
        });
                contrast_button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double s1=Double.parseDouble(s1T.getCharacters().toString());
                double s2=Double.parseDouble(s2T.getCharacters().toString());
                double r1=Double.parseDouble(r1T.getCharacters().toString());
                double r2=Double.parseDouble(r2T.getCharacters().toString());
                
                double [] lookTable2=new double [256];
                for(int y = 0; y < 256; y++) {
                if(y<r1)
                     lookTable2[y]=((double)y)/255*s1/r1;
                else if(y>=r1&&y<=r2)
                     lookTable2[y]=((((double)y)-r1)*(s2-s1)/(r2-r1)+s1)/255;
                else if(y>=r1)
                     lookTable2[y]=((((double)y)-r2)*(255-s2)/(255-r2)+s2)/255;
                 }
                Image gamma_image=ImageContrast(imageView.getImage(),lookTable2);
                imageView.setImage(gamma_image);
                System.out.print("Gata!");
            }
        });
                //Question 3. The buttons "Histogram Red","Histogram Green","Histogram BLue","Histogram Grey" will display the histogram of the image
                //before equalization. The button "Histogram Equalization" will equalize the image and display its histogram.
		final int[][] histogram=Histogram(imageView.getImage());
     
		histogram_button_red.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Color Histogram");
                xAxis.setLabel("Pixel");       
                yAxis.setLabel("Value of Pixels");
                
                
                XYChart.Series series1 = new XYChart.Series();
                series1.setName("red");    
                //bc.setStyle("-fx-bar-fill: red;");
                for(int y = 0; y < 256; y++) {
                series1.getData().add(new XYChart.Data(String.valueOf(y), histogram[y][0]));
                }    
                
                Stage window = new Stage();
                Scene s = new Scene(bc, 800, 600, Color.WHITE);                
                bc.getData().addAll(series1);
                window.setScene(s);
                window.show();           
            }
        });
		histogram_button_blue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
	
                
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Color Histogram");
                xAxis.setLabel("Pixel");       
                yAxis.setLabel("Value of Pixels");                
                
                XYChart.Series series1 = new XYChart.Series();
                series1.setName("blue"); 
                //bc.setStyle("-fx-bar-fill: blue;");
                for(int y = 0; y < 256; y++) {
                series1.getData().add(new XYChart.Data(String.valueOf(y), histogram[y][2]));
                }    
                
                Stage window = new Stage();
                Scene s = new Scene(bc, 800, 600, Color.WHITE);                
                bc.getData().addAll(series1);
                window.setScene(s);
                window.show();           
            }
        });
                histogram_button_green.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Color Histogram");
                xAxis.setLabel("Pixel");       
                yAxis.setLabel("Value of Pixels");
                
                
                XYChart.Series series1 = new XYChart.Series();
                series1.setName("green");    
               // bc.setStyle("-fx-bar-fill: green;");
                for(int y = 0; y < 256; y++) {
                series1.getData().add(new XYChart.Data(String.valueOf(y), histogram[y][1]));
                }    
                
                Stage window = new Stage();
                Scene s = new Scene(bc, 800, 600, Color.WHITE);                
                bc.getData().addAll(series1);
                window.setScene(s);
                window.show();           
            }
        });
                histogram_button_grey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Color Histogram");
                xAxis.setLabel("Pixel");       
                yAxis.setLabel("Value of Pixels");
                
                
                XYChart.Series series1 = new XYChart.Series();
                series1.setName("grey");    
                //bc.setStyle("-fx-bar-fill: grey;");
                for(int y = 0; y < 256; y++) {
                series1.getData().add(new XYChart.Data(String.valueOf(y), histogram[y][3]));
                }    
                
                Stage window = new Stage();
                Scene s = new Scene(bc, 800, 600, Color.WHITE);                
                bc.getData().addAll(series1);
                window.setScene(s);
                window.show();           
            }
        });
                histogram_button_equal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              	

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Color Histogram");
                xAxis.setLabel("Pixel");       
                yAxis.setLabel("Value of Pixels");              
                
                XYChart.Series series1 = new XYChart.Series();
                series1.setName("equalization");    
                
       
                double [] map=new double[256];
                for(int y = 0; y < 256; y++) {
                    map[y]=histogram[y][3];
                }
                for(int y = 0; y < 256; y++) {
                    if(y==0)
                        map[0]=map[0];
                    else
                    map[y]=map[y-1]+map[y];
                }

                for(int y = 0; y < 256; y++) {
                    //size=number of total pixels of the image
                    map[y]=(int)Math.round((map[y]*255.0)/image.getWidth()/image.getHeight());
                }   
                
                WritableImage inverted_image = new WritableImage((int)image.getWidth(),(int) image.getHeight());
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
                PixelReader image_reader=image.getPixelReader(); 
                for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				
				Color color = image_reader.getColor(x, y);
                                int grey=(int)((color.getRed()+color.getGreen()+color.getBlue())*255/3);

                                color=Color.color(map[grey]/255.0,map[grey]/255.0,map[grey]/255.0);
                                
				inverted_image_writer.setColor(x, y, color);
				
                        }
                }
                imageView.setImage(inverted_image);
                         
                for(int y = 0; y < 256; y++) {
                series1.getData().add(new XYChart.Data(String.valueOf(y), map[y]));
                }    
                
                Stage window = new Stage();
                Scene s = new Scene(bc, 800, 600, Color.WHITE);                
                bc.getData().addAll(series1);
                window.setScene(s);
                window.show();           
            }
        });
                //Question 4. The button "Cross Correlation" will apply the cross correlation algorithm to the image.
		cc_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                int [][] A={{-4,-1,0,-1,-4},{-1,2,3,2,-1},{0,3,4,3,0},{-1,2,3,2,-1},{-4,-1,0,-1,-4}};
                imageView.setImage(CrossCorelation(imageView.getImage(),A));
                                 
            }
        });
                //reset image button
                reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               	imageView.setImage(image);
            }
        });		
		//Using a flow pane
		FlowPane root = new FlowPane();
		//Gaps between buttons
		root.setVgap(10);
                root.setHgap(5);
		//Add all the buttons and the image for the GUI
                Label a=new Label("                                                         ");
                root.getChildren().addAll(invert_button, gamma_button, contrast_button,contrast_button1, histogram_button_red, histogram_button_green, histogram_button_blue, histogram_button_grey,histogram_button_equal, cc_button,reset, imageView,a,gammaT,r1T,s1T,r2T,s2T);     
		//Display to user
                Scene scene = new Scene(root, 1024, 800);
                stage.setScene(scene);
                stage.show();
    }

	//Example function of invert
	public Image ImageInverter(Image image) {
		//Find the width and height of the image to be process
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
		//Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		//Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		//Get an interface to read from the original image passed as the parameter to the function
		PixelReader image_reader=image.getPixelReader();
		
		//Iterate over all pixels
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				//For each pixel, get the colour
				Color color = image_reader.getColor(x, y);
				//Do something (in this case invert) - the getColor function returns colours as 0..1 doubles (we could multiply by 255 if we want 0-255 colours)
				color=Color.color(1.0-color.getRed(), 1.0-color.getGreen(), 1.0-color.getBlue());
				//Note: for gamma correction you may not need the divide by 255 since getColor already returns 0-1, nor may you need multiply by 255 since the Color.color function consumes 0-1 doubles.
				
				//Apply the new colour
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
	}
        //Example function of gamma correction
	public Image ImageGC(Image image,double gamma) {
		//Find the width and height of the image to be process
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
		//Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		//Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		//Get an interface to read from the original image passed as the parameter to the function
		PixelReader image_reader=image.getPixelReader();
               
		//Iterate over all pixels
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				//For each pixel, get the colour
				Color color = image_reader.getColor(x, y);
				//Do something (in this case invert) - the getColor function returns colours as 0..1 doubles (we could multiply by 255 if we want 0-255 colours)
				color=Color.color(Math.pow(color.getRed(),1.0/gamma), Math.pow(color.getGreen(),1.0/gamma), Math.pow(color.getBlue(),1.0/gamma));
				//Note: for gamma correction you may not need the divide by 255 since getColor already returns 0-1, nor may you need multiply by 255 since the Color.color function consumes 0-1 doubles.
				
				//Apply the new colour
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
	}
        public Image ImageGC2(Image image,double gamma,double [] table) {
                //Find the width and height of the image to be process
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
		//Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		//Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		//Get an interface to read from the original image passed as the parameter to the function
		PixelReader image_reader=image.getPixelReader();
		
                //Iterate over all pixels
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				Color color = image_reader.getColor(x, y);
					
				color=Color.color(table[(int)(255*color.getRed())],table[(int)(255*color.getGreen())],table[(int)(255*color.getBlue())]);
                                
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
        }
        public Image ImageContrast(Image image,double [] table) {
                //Find the width and height of the image to be process
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
		//Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		//Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		//Get an interface to read from the original image passed as the parameter to the function
		PixelReader image_reader=image.getPixelReader();
		
                //Iterate over all pixels
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				Color color = image_reader.getColor(x, y);
					
				color=Color.color(table[(int)(255*color.getRed())],table[(int)(255*color.getGreen())],table[(int)(255*color.getBlue())]);
                                
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
        }
        public int[][] Histogram(Image image) {
                
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();		
		WritableImage inverted_image = new WritableImage(width, height);		
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();		
		PixelReader image_reader=image.getPixelReader();
                
                int[][] histogram=new int[256][4];    
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				
				Color color = image_reader.getColor(x, y);				
				
                                int red=(int)(color.getRed()*255);
                                int green=(int)(color.getGreen()*255);
                                int blue=(int)(color.getBlue()*255);
                                int grey=(int)((color.getRed()+color.getGreen()+color.getBlue())*255.0/3.0);
				histogram[red][0]++;
                                histogram[green][1]++;
                                histogram[blue][2]++;
                                histogram[grey][3]++;
                     
			}
		}
		return histogram;
        }
        public Image CrossCorelation(Image image,int [][] a) {
int width = (int)image.getWidth();
                int height = (int)image.getHeight();
                //System.out.println(height);//455
                //System.out.println(width);//634
		WritableImage inverted_image = new WritableImage(width, height);
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		PixelReader image_reader=image.getPixelReader();
                int [][]red=new int[height][width];
                int [][]green=new int[height][width];
                int [][]blue=new int[height][width];
                int min=1000000000,max=-1000000000;        
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
                            
                                if(i>1&&j>1&&i<height-2&&j<width-2){    
                                 
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            red[i][j]=red[i][j]+a[x][y]*(int)(color.getRed()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){                                 
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            green[i][j]=green[i][j]+a[x][y]*(int)(color.getGreen()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            blue[i][j]=blue[i][j]+a[x][y]*(int)(color.getBlue()*255);
                                                    }
                                    }                                   
                                    
                                    max=Math.max(max,green[i][j]);
                                    max=Math.max(red[i][j],max);
                                    max=Math.max(max, blue[i][j]);
                                    
                                    min=Math.min(min,green[i][j]);                                    
                                    min=Math.min(red[i][j], min);
                                    min=Math.min(min, blue[i][j]);  

                                }
			}
		}

                for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
                          
                            if(!(i>1&&j>1&&i<height-2&&j<width-2)){
                                //black out margins
                                inverted_image_writer.setColor(j, i, Color.color(1,1,1));                                
                            }else{

                                double redN=1.0*(red[i][j]-min)/(max-min);
                                double greenN=1.0*(green[i][j]-min)/(max-min);
                                double blueN=1.0*(blue[i][j]-min)/(max-min);
                                inverted_image_writer.setColor( j,i, Color.color(redN,greenN,blueN));
                            }
                        }
                }
                
                
                
		return inverted_image;
        }
        
         public static void main(String[] args) {
                launch();
        }

}
/*

 public Image CrossCorelation(Image image,int [][] a) {
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
                //System.out.println(height);//455
                //System.out.println(width);//634
		WritableImage inverted_image = new WritableImage(width, height);
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		PixelReader image_reader=image.getPixelReader();
                int [][]red=new int[height][width];
                int [][]green=new int[height][width];
                int [][]blue=new int[height][width];
                int min=1000000000,max=-1000000000;        
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
                            
                                if(i>1&&j>1&&i<height-2&&j<width-2){    
                                 
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            red[i][j]=red[i][j]+a[x][y]*(int)(color.getRed()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){                                 
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            green[i][j]=green[i][j]+a[x][y]*(int)(color.getGreen()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y-2, i+x-2);                                        
                                            blue[i][j]=blue[i][j]+a[x][y]*(int)(color.getBlue()*255);
                                                    }
                                    }                                   
                                    
                                    max=Math.max(max,green[i][j]);
                                    max=Math.max(red[i][j],max);
                                    max=Math.max(max, blue[i][j]);
                                    
                                    min=Math.min(min,green[i][j]);                                    
                                    min=Math.min(red[i][j], min);
                                    min=Math.min(min, blue[i][j]);  

                                }
			}
		}

                for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
                          
                            if(!(i>1&&j>1&&i<height-2&&j<width-2)){
                                //black out margins
                                inverted_image_writer.setColor(j, i, Color.color(1,1,1));                                
                            }else{

                                double redN=1.0*(red[i][j]-min)/(max-min);
                                double greenN=1.0*(green[i][j]-min)/(max-min);
                                double blueN=1.0*(blue[i][j]-min)/(max-min);
                                inverted_image_writer.setColor( j,i, Color.color(redN,greenN,blueN));
                            }
                        }
                }
*/
/*
		int width = (int)image.getWidth();
                int height = (int)image.getHeight();
                //System.out.println(height);//455
                //System.out.println(width);//634
		WritableImage inverted_image = new WritableImage(width, height);
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		PixelReader image_reader=image.getPixelReader();
                int [][]red=new int[height][width];
                int [][]green=new int[height][width];
                int [][]blue=new int[height][width];
                int min=1000000000,max=-1000000000;        
		
		for(int i = 0; i < height-4; i++) {
			for(int j = 0; j < width-4; j++) {
                            
                                
                                 
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y, i+x);                                        
                                            red[i][j]=red[i][j]+a[x][y]*(int)(color.getRed()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){                                 
                                            Color color = image_reader.getColor(j+y, i+x);                                        
                                            green[i][j]=green[i][j]+a[x][y]*(int)(color.getGreen()*255);
                                                    }
                                    }
                                    for(int x = 0; x < 5; x++) {
                                        for(int y = 0; y < 5; y++){
                                            Color color = image_reader.getColor(j+y, i+x);                                        
                                            blue[i][j]=blue[i][j]+a[x][y]*(int)(color.getBlue()*255);
                                                    }
                                    }                                   
                                    
                                    max=Math.max(max,green[i][j]);
                                    max=Math.max(red[i][j],max);
                                    max=Math.max(max, blue[i][j]);
                                    
                                    min=Math.min(min,green[i][j]);                                    
                                    min=Math.min(red[i][j], min);
                                    min=Math.min(min, blue[i][j]);  

                                
			}
		}

                for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
                          
                            if(!(i>1&&j>1&&i<height-2&&j<width-2)){
                                //black out margins
                                inverted_image_writer.setColor(j, i, Color.color(1,1,1));                                
                            }else{

                                double redN=1.0*(red[i-2][j-2]-min)/(max-min);
                                double greenN=1.0*(green[i-2][j-2]-min)/(max-min);
                                double blueN=1.0*(blue[i-2][j-2]-min)/(max-min);
                                inverted_image_writer.setColor( j,i, Color.color(redN,greenN,blueN));
                            }
                        }
                }

*/
package VoiceAssistant;

import processing.core.PApplet;

public class VoiceAssistantGUI extends PApplet {

  public static void main(String[] args) {
    PApplet.main("VoiceAssistant.VoiceAssistantGUI");
  }

  int rectX, rectY;      // Position of square button
  int circleX, circleY;  // Position of circle button
  int rectSize = 90;     // Diameter of rect
  int circleSize = 93;   // Diameter of circle
  int rectColor, circleColor;
  int rectHighlight, circleHighlight;
  boolean rectOver = false;
  boolean circleOver = false;
  boolean onText = false;
  boolean offText = true;
  SpeechRecognition assistant;

  public void settings() {
    size(370, 300);
  }

  public void setup() {
    assistant = new SpeechRecognition();
    assistant.ignoreSpeechRecognitionResults();
    rectColor = color(240, 0, 0);
    rectHighlight = color(204, 0, 0);
    circleColor = color(0, 240, 0);
    circleHighlight = color(0, 204, 0);
    circleX = width/2+circleSize/2+10;
    circleY = height/2;
    rectX = width/2-rectSize-10;
    rectY = height/2-rectSize/2;
    ellipseMode(CENTER);
  }

  public void draw() {
    update(mouseX, mouseY);
    background(0);

    if(offText)
      offText();
    if(onText)
      onText();

    if(rectOver) {
      fill(rectHighlight);
    } else {
      fill(rectColor);
    }
    rect(rectX, rectY, rectSize, rectSize);

    if(circleOver) {
      fill(circleHighlight);
    } else {
      fill(circleColor);
    }
    stroke(0);
    ellipse(circleX, circleY, circleSize, circleSize);
  }

  public void update(int x, int y) {
    if(overCircle(circleX, circleY, circleSize)) {
      circleOver = true;
      rectOver = false;
    } else if(overRect(rectX, rectY, rectSize, rectSize)) {
      rectOver = true;
      circleOver = false;
    } else {
      circleOver = rectOver = false;
    }
  }

  public void mousePressed() {
    if(rectOver) {
      offText = true;
      onText = false;
      assistant.ignoreSpeechRecognitionResults();
    }
    if(circleOver) {
      onText = true;
      offText = false;
      assistant.stopIgnoreSpeechRecognitionResults();
    }
  }

  public void offText() {
    textSize(24);
    fill(204, 0, 0);
    textAlign(CENTER, CENTER);
    text("Microphone is off", 180, 60);
  }

  public void onText() {
    textSize(24);
    fill(0, 204, 0);
    textAlign(CENTER, CENTER);
    text("Microphone is on", 180, 60);
  }

  public boolean overRect(int x, int y, int width, int height)  {
    if(mouseX >= x && mouseX <= x+width &&
        mouseY >= y && mouseY <= y+height) {
      return true;
    } else {
      return false;
    }
  }

  public boolean overCircle(int x, int y, int diameter) {
    float disX = x - mouseX;
    float disY = y - mouseY;
    if(sqrt(sq(disX) + sq(disY)) < diameter/2) {
      return true;
    } else {
      return false;
    }
  }

}

package VoiceAssistant;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;

public class SpeechRecognition {

    private LiveSpeechRecognizer recognizer;
    private String speechRecognitionResult;
    private boolean ignoreSpeechRecognitionResults = true;
    private boolean speechRecognizerThreadRunning = false;
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);

    SpeechRecognition() {
    	new VoiceAssistant();
        main();
    }

    public void main() {
        System.out.println("Loading Speech Recognizer...\n");

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            System.out.println(e);
        }

        startSpeechRecognition();
    }

    public void startSpeechRecognition() {
        if(speechRecognizerThreadRunning)
            System.out.println("Speech Recognition Thread already running\n");
        else
            eventsExecutorService.submit(() -> {
                speechRecognizerThreadRunning = true;
                recognizer.startRecognition(true);
                System.out.println("You can start to speak\n");
                try {
                    while(speechRecognizerThreadRunning) {
                        SpeechResult speechResult = recognizer.getResult();

                        if(!ignoreSpeechRecognitionResults) {
                            if (speechResult == null)
                                System.out.println("Speech was not understood\n");
                            else {
                                speechRecognitionResult = speechResult.getHypothesis();

                                System.out.println("You said: " + speechRecognitionResult + "\n");
                                makeDecision(speechRecognitionResult, speechResult.getWords());
                            }
                        }
                    }
                } catch(Exception e) {
                    System.out.println(e);
                    speechRecognizerThreadRunning = false;
                }

                System.out.println("SpeechThread has exited");

            });
    }
    
    public void makeDecision(String speech, List<WordResult> speechWords) throws IOException, InterruptedException, StringIndexOutOfBoundsException {
        if(speech.contains("search ")) {
        	VoiceAssistant.googleSearch(speech.substring(speech.indexOf("search")+7));
			Thread.sleep(100);
        }
        
        else if(speech.contains("stop search")) {
        	VoiceAssistant.closeChrome(speech.substring(speech.indexOf("stop")+11));
        }
    }
    
    public synchronized void stopIgnoreSpeechRecognitionResults() {
		ignoreSpeechRecognitionResults = false;
	}
	
	public synchronized void ignoreSpeechRecognitionResults() {
		ignoreSpeechRecognitionResults = true;
	}
}

//package alexaSkill
//
//import com.amazon.speech.speechlet.IntentRequest
//import com.amazon.speech.speechlet.LaunchRequest
//import com.amazon.speech.speechlet.Session
//import com.amazon.speech.speechlet.SessionEndedRequest
//import com.amazon.speech.speechlet.SessionStartedRequest
//import com.amazon.speech.speechlet.SpeechletException
//import com.amazon.speech.speechlet.SpeechletResponse
//import com.amazon.speech.ui.LinkAccountCard
//import com.amazon.speech.ui.PlainTextOutputSpeech
//import com.amazon.speech.ui.Reprompt
//import com.amazon.speech.ui.SimpleCard
//import com.amazon.speech.speechlet.Speechlet
//import grails.config.Config
//import grails.core.support.GrailsConfigurationAware
//import grails.web.Controller
//import groovy.util.logging.Slf4j
//
//
//@Slf4j
//class AlexaSpeechlet implements GrailsConfigurationAware, Speechlet {
//
//    def grailsApplication
//
//    Config grailsConfig
//    def speechletService
//
//
//    def index() {
//        speechletService.doSpeechlet(request,response, this)
//    }
//
//    /**
//     * This is called when the session is started
//     * Add an initialization setup for the session here
//     * @param request SessionStartedRequest
//     * @param session Session
//     * @throws SpeechletException
//     */
//    public void onSessionStarted(final SessionStartedRequest request, final Session session)
//            throws SpeechletException {
//        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
//                session.getSessionId())
//
//    }
//
//    /**
//     * This is called when the skill/speechlet is launched on Alexa
//     * @param request LaunchRequest
//     * @param session Session
//     * @return
//     * @throws SpeechletException
//     */
//    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
//            throws SpeechletException {
//        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
//                session.getSessionId())
//
//        return getWelcomeResponse()
//    }
//
//    SpeechletResponse getBalance(final IntentRequest request, final Session session) {
//        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
//        // Create the Simple card content.
//        SimpleCard card = new SimpleCard(title:"Edward Jones Balance")
//        def speechText = "Your balance is \$1,000,000.00"
//        def cardText = "Your balance is \$1,000,000.00"
//        // Create the plain text output.
//        speech.setText(speechText)
//        card.setContent(cardText)
//        SpeechletResponse.newTellResponse(speech, card)
//    }
//
//    /**
//     * This is the method fired when an intent is called
//     *
//     * @param request IntentRequest intent called from Alexa
//     * @param session Session
//     * @return SpeechletResponse tell or ask type
//     * @throws SpeechletException
//     */
//    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
//            throws SpeechletException {
//        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
//                session.getSessionId())
//
//        log.debug("invoking intent:${intentName}")
//        switch (request.intent) {
//            case 'GetBalanceIntent':
//                getBalance(request, session)
//                break;
//            default:
//                PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
//                // Create the Simple card content.
//                SimpleCard card = new SimpleCard(title:"Edward Jones Results")
//                def speechText = "I will say something"
//                def cardText = "I will print something"
//                // Create the plain text output.
//                speech.setText(speechText)
//                card.setContent(cardText)
//                SpeechletResponse.newTellResponse(speech, card)
//        }
//
//    }
//    /**
//     * Grails config is injected here for configuration of your speechlet
//     * @param co Config
//     */
//    void setConfiguration(Config co) {
//        this.grailsConfig = co
//    }
//
//    /**
//     * this is where you do session cleanup
//     * @param request SessionEndedRequest
//     * @param session
//     * @throws SpeechletException
//     */
//    public void onSessionEnded(final SessionEndedRequest request, final Session session)
//            throws SpeechletException {
//        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
//                session.getSessionId())
//        // any cleanup logic goes here
//    }
//
//    SpeechletResponse getWelcomeResponse()  {
//        String speechText = "Welcome to Edward Jones. " +
//                "You can ask about your balance, the latest market update, account performance or " +
//                "send a message to your branch."
//
//        // Create the Simple card content.
//        SimpleCard card = new SimpleCard(title: "Welcome to Edward Jones.", content: speechText)
//
//        // Create the plain text output.
//        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
//
//        // Create reprompt
//        Reprompt reprompt = new Reprompt(outputSpeech: speech)
//
//        SpeechletResponse.newAskResponse(speech, reprompt, card)
//    }
//
//    /**
//     * default responder when a help intent is launched on how to use your speechlet
//     * @return
//     */
//    SpeechletResponse getHelpResponse() {
//        String speechText = "You can ask about your balance, the latest market update, account performance or " +
//                "send a message to your branch."
//        // Create the Simple card content.
//        SimpleCard card = new SimpleCard(title:"Edward Jones Help",
//                content:speechText)
//        // Create the plain text output.
//        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
//        // Create reprompt
//        Reprompt reprompt = new Reprompt(outputSpeech: speech)
//        SpeechletResponse.newAskResponse(speech, reprompt, card)
//    }
//
//    /**
//     * if you are using account linking, this is used to send a card with a link to your app to get started
//     * @param session
//     * @return
//     */
//    SpeechletResponse createLinkCard(Session session) {
//
//        String speechText = "Please use the alexa app to link account."
//        // Create the Simple card content.
//        LinkAccountCard card = new LinkAccountCard()
//        // Create the plain text output.
//        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
//        log.debug("Session ID=${session.sessionId}")
//        // Create reprompt
//        Reprompt reprompt = new Reprompt(outputSpeech: speech)
//        SpeechletResponse.newTellResponse(speech, card)
//    }
//
//}
//
//
///**
// * this inner controller handles incoming requests - be sure to white list it with SpringSecurity
// * or whatever you are using
// */
//@Controller
//class AlexaController {
//
//    def speechletService
//    def alexaSpeechlet
//
//
//    def index() {
//        speechletService.doSpeechlet(request,response, alexaSpeechlet)
//    }
//
//}

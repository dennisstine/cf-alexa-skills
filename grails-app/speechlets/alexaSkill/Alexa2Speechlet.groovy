package alexaSkill

import com.amazon.speech.slu.Slot
import com.amazon.speech.speechlet.IntentRequest
import com.amazon.speech.speechlet.LaunchRequest
import com.amazon.speech.speechlet.Session
import com.amazon.speech.speechlet.SessionEndedRequest
import com.amazon.speech.speechlet.SessionStartedRequest
import com.amazon.speech.speechlet.SpeechletException
import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.LinkAccountCard
import com.amazon.speech.ui.PlainTextOutputSpeech
import com.amazon.speech.ui.Reprompt
import com.amazon.speech.ui.SimpleCard
import com.amazon.speech.speechlet.Context
import com.amazon.speech.speechlet.PlaybackFailedRequest
import com.amazon.speech.speechlet.PlaybackFinishedRequest
import com.amazon.speech.speechlet.PlaybackNearlyFinishedRequest
import com.amazon.speech.speechlet.PlaybackStartedRequest
import com.amazon.speech.speechlet.PlaybackStoppedRequest
import com.amazon.speech.speechlet.SystemExceptionEncounteredRequest
import com.amazon.speech.speechlet.Speechlet
import com.amazon.speech.ui.SsmlOutputSpeech
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.web.Controller
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class Alexa2Speechlet implements GrailsConfigurationAware, Speechlet {

    def grailsApplication

    Config grailsConfig
    def speechletService


    def index() {
        speechletService.doSpeechlet(request,response, this)
    }


    @Override
    SpeechletResponse onPlaybackStarted(PlaybackStartedRequest playbackStartedRequest, Context context) throws SpeechletException {
        return null
    }

    @Override
    SpeechletResponse onPlaybackFinished(PlaybackFinishedRequest playbackFinishedRequest, Context context) throws SpeechletException {
        return null
    }

    @Override
    void onPlaybackStopped(PlaybackStoppedRequest playbackStoppedRequest, Context context) throws SpeechletException {

    }

    @Override
    SpeechletResponse onPlaybackNearlyFinished(PlaybackNearlyFinishedRequest playbackNearlyFinishedRequest, Context context) throws SpeechletException {
        return null
    }

    @Override
    SpeechletResponse onPlaybackFailed(PlaybackFailedRequest playbackFailedRequest, Context context) throws SpeechletException {
        return null
    }

    @Override
    void onSystemException(SystemExceptionEncounteredRequest systemExceptionEncounteredRequest) throws SpeechletException {

    }

    /**
     * This is called when the session is started
     * Add an initialization setup for the session here
     * @param request SessionStartedRequest
     * @param session Session
     * @throws SpeechletException
     */
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())

    }

    /**
     * This is called when the skill/speechlet is launched on Alexa
     * @param request LaunchRequest
     * @param session Session
     * @return
     * @throws SpeechletException
     */
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())

        return getWelcomeResponse()
    }

    SpeechletResponse getBalance(final IntentRequest request, final Session session, Context context) {
        SsmlOutputSpeech speech = new SsmlOutputSpeech()
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones Balance")
        def slot = request.intent.getSlot("Nickname")
        log.info("invoking balance for :${slot.value}")

        String balance = null
        String response = null
        def speechText = "<speak>I'm sorry, but we could not process your request right now.</speak>"
        def cardText = "I'm sorry, but we could not process your request right now."
        if (slot) {
            response = "http://gateway.ejcodefest2016.com:8080/gateway/balance/${slot.value}".toURL().text
            if (response != null) {
                def slurper = new JsonSlurper()
                def result = slurper.parseText(response)

                speechText = "<speak>Your balance is <say-as interpret-as=\"unit\">\$${result.acctBalance}</say-as></speak>"
                cardText = "Your balance is \$${result.acctBalance}"
            }
        }
        else {
            response = 'http://gateway.ejcodefest2016.com:8080/gateway/balances/'.toURL().text
            if (response != null) {
                speechText = "<speak>Your balance is <say-as interpret-as=\"unit\">\$${response}</say-as></speak>"
                cardText = "Your balance is \$${response}"
            }
        }
        // Create the plain text output.
        speech.setSsml(speechText)
        card.setContent(cardText)
        def response1 = SpeechletResponse.newTellResponse(speech, card)
        response1.shouldEndSession = false
        response1
    }

    SpeechletResponse getPerformance(final IntentRequest request, final Session session, Context context) {
        SsmlOutputSpeech speech = new SsmlOutputSpeech()
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones Performance")
        def slot = request.intent.getSlot("Nickname")
        log.info("invoking performance for :${slot.value}")

        String response = null
        if (slot) {
            response = "http://gateway.ejcodefest2016.com:8080/gateway/performance/${slot.value}".toURL().text
        }
        def speechText = "<speak>I'm sorry, but we could not process your request right now.</speak>"
        def cardText = "I'm sorry, but we could not process your request right now."
        if (response != null && !response.isEmpty()) {
            def slurper = new JsonSlurper()
            def result = slurper.parseText(response)

            speechText = "<speak>Your performance year to date is <say-as interpret-as=\"unit\">${result.ytdPct*100}%</say-as></speak>"
            cardText = "Your performance year to date is ${result.ytdPct * 100}%"
        }
        // Create the plain text output.
        speech.setSsml(speechText)
        card.setContent(cardText)
        def response1 = SpeechletResponse.newTellResponse(speech, card)
        response1.shouldEndSession = false
        response1
    }

    SpeechletResponse getMarketUpdate(final IntentRequest request, final Session session, Context context) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones Market Update")
        String response = null
        response = 'http://gateway.ejcodefest2016.com:8080/gateway/marketupdate'.toURL().text
        def speechText = "I'm sorry, but we could not process your request right now."
        def cardText = "I'm sorry, but we could not process your request right now."
        if (response != null) {
            def slurper = new JsonSlurper()
            def result = slurper.parseText(response)

            speechText = "Today's market update is ${result.summary}"
            cardText = "Today's market update is ${result.summary}"
        }
        // Create the plain text output.
        speech.setText(speechText)
        card.setContent(cardText)
        def response1 = SpeechletResponse.newTellResponse(speech, card)
        response1.shouldEndSession = false
        response1
    }

    /**
     * This is the method fired when an intent is called
     *
     * @param request IntentRequest intent called from Alexa
     * @param session Session
     * @return SpeechletResponse tell or ask type
     * @throws SpeechletException
     */
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session, Context context)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())


        String intentName = request.intent.name
        log.debug("invoking intent:${intentName}")
        switch (intentName) {
            case 'GetBalanceIntent':
                getBalance(request, session, context)
                break;
            case 'GetMarketUpdate':
                getMarketUpdate(request, session, context)
                break;
            case 'GetPerformanceIntent':
                getPerformance(request, session, context)
                break;
            case 'AMAZON.HelpIntent':
                getHelpResponse()
                break;
            case 'AMAZON.StopIntent':
                getStopResponse()
                break;
            default:
                getUnknownResponse()
        }

    }
    /**
     * Grails config is injected here for configuration of your speechlet
     * @param co Config
     */
    void setConfiguration(Config co) {
        this.grailsConfig = co
    }

    /**
     * this is where you do session cleanup
     * @param request SessionEndedRequest
     * @param session
     * @throws SpeechletException
     */
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())
        // any cleanup logic goes here
    }

    SpeechletResponse getWelcomeResponse()  {
        String speechText = "Welcome to Edward Jones. " +
                "You can ask about your balance, the latest market update, account performance or " +
                "send a message to your branch."

        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title: "Welcome to Edward Jones.", content: speechText)

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)

        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)

        SpeechletResponse.newAskResponse(speech, reprompt, card)
    }

    SpeechletResponse getUnknownResponse() {
        def speechText = "I didn't understand your request. You can ask about your balance, the latest " +
                "market update, account performance or " +
                "send a message to your branch."
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones",
                content:speechText)
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        def response1 = SpeechletResponse.newTellResponse(speech, card)
        response1.shouldEndSession = false
        response1
    }

    SpeechletResponse getStopResponse() {
        String speechText = "Thank you. If you need other assistance please call your branch. Have a nice day."
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones",
                content:speechText)
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)
        SpeechletResponse.newTellResponse(speech, card)
    }

    /**
     * default responder when a help intent is launched on how to use your speechlet
     * @return
     */
    SpeechletResponse getHelpResponse() {
        String speechText = "You can ask about your balance, the latest market update, account performance or " +
                "send a message to your branch."
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Edward Jones Help",
                content:speechText)
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)
        SpeechletResponse.newAskResponse(speech, reprompt, card)
    }

    /**
     * if you are using account linking, this is used to send a card with a link to your app to get started
     * @param session
     * @return
     */
    SpeechletResponse createLinkCard(Session session) {

        String speechText = "Please use the alexa app to link account."
        // Create the Simple card content.
        LinkAccountCard card = new LinkAccountCard()
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        log.debug("Session ID=${session.sessionId}")
        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)
        SpeechletResponse.newTellResponse(speech, card)
    }

}


/**
 * this inner controller handles incoming requests - be sure to white list it with SpringSecurity
 * or whatever you are using
 */
@Controller
class Alexa2Controller {

    def speechletService
    def alexa2Speechlet


    def index() {
        speechletService.doSpeechlet(request,response, alexa2Speechlet)
    }

}
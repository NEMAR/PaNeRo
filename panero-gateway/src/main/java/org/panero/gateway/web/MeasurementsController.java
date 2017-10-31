package org.panero.gateway.web;

import javax.validation.Valid;

import org.panero.common.model.BatchMeasurements;
import org.panero.common.model.api.Measurements;
import org.panero.gateway.config.MessageFlowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/write")
public class MeasurementsController {
    private static Logger logger = LoggerFactory.getLogger(MeasurementsController.class);

    @Autowired
    private MessageFlowConfiguration.MeasurementsGateway gateway;

    /**
     * POST /write
     *
     * @param payload Request body with a {@link BatchMeasurements} object
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void write(@Valid @RequestBody final Measurements payload) {
        logger.debug("POST /write - Payload with {} measurements", payload.getMeasurements().size());
        gateway.send(payload);
    }
}

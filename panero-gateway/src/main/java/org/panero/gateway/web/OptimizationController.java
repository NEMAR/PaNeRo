package org.panero.gateway.web;

import org.panero.common.BrokerConstants;
import org.panero.common.model.Command;
import org.panero.common.model.NameValuePair;
import org.panero.gateway.config.MessageFlowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/optimization")
public class OptimizationController {
    private static Logger logger = LoggerFactory.getLogger(OptimizationController.class);

    @Autowired
    private MessageFlowConfiguration.CommandGateway gateway;

    /**
     * POST /optimization/prepare
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, path = "/prepare")
    public String prepare() {
        logger.debug("POST /optimization/prepare - Sending command to prepare optimization");
        return gateway.send(Command.create(BrokerConstants.COMMAND_PREPARE_OPTIMIZATION).and().buildIt());
    }
}

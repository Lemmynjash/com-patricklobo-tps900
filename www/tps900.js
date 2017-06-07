/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

/**
 * This represents the mobile tps900, and provides properties for inspecting the model, version, UUID of the
 * phone, etc.
 * @constructor
 */
function Tps900() {
    this.buffer = [];

    this.inicia = () => {
        this.buffer = [];
    }

    this.addString = (value) => {
        this.buffer.push({
            addString: {
                value: value
            }
        });
    }
    this.setAlgin = (value) => {
        this.buffer.push({
            setAlgin: {
                value: value
            }
        });
    }
    this.setBold = (value) => {
        this.buffer.push({
            setBold: {
                value: value
            }
        });
    }
    this.setCharSpace = (value) => {
        this.buffer.push({
            setCharSpace: {
                value: value
            }
        });
    }
    this.setFontSize = (value) => {
        this.buffer.push({
            setFontSize: {
                value: value
            }
        });
    }
    this.setGray = (value) => {
        this.buffer.push({
            setGray: {
                value: value
            }
        });
    }
    this.setHighlight = (value) => {
        this.buffer.push({
            setHighlight: {
                value: value
            }
        });
    }
    this.setLineSpace = (value) => {
        this.buffer.push({
            setLineSpace: {
                value: value
            }
        });
    }
    this.walkPaper = (value) => {
        this.buffer.push({
            walkPaper: {
                value: value
            }
        });
    }
    this.printStringAndWalk = (d, m, l) => {
        this.buffer.push({
            printStringAndWalk: {
                direction: d,
                mode: m,
                lines: l
            }
        });
    }
    this.textAsBitmap = (texto, font_grande, negrito) => {
        this.buffer.push({
            textAsBitmap: {
                texto: texto,
                fontGrande: font_grande,
                negrito: negrito
            }
        });
    }
    this.printQrcode = (texto, w, y) => {
        this.buffer.push({
            printQrcode: {
                value: texto,
                w: w,
                y: y
            }
        });
    }
     this.enlargeFontSize = (w, h) => {
        this.buffer.push({
            enlargeFontSize: {
                widthMultiple: w,
                heightMultiple: h
            }
        });
    }
    this.printString = () => {
        this.buffer.push({
            printString: true
        });
    }
    this.clearString = () => {
        this.buffer.push({
            clearString: true
        });
    }
    this.reset = () => {
        this.buffer.push({
            reset: true
        });
    }

    this.imprime = function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, "Tps900", "imprime", [this.buffer]);
    }

}

// Tps900.prototype.;

module.exports = new Tps900();

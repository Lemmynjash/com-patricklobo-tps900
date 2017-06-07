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

/* jshint jasmine: true */

exports.defineAutoTests = function() {
  describe('Tps900 Information (window.tps900)', function () {
    it("should exist", function() {
      expect(window.tps900).toBeDefined();
    });

    it("should contain a platform specification that is a string", function() {
      expect(window.tps900.platform).toBeDefined();
      expect((String(window.tps900.platform)).length > 0).toBe(true);
    });

    it("should contain a version specification that is a string", function() {
      expect(window.tps900.version).toBeDefined();
      expect((String(window.tps900.version)).length > 0).toBe(true);
    });

    it("should contain a UUID specification that is a string or a number", function() {
      expect(window.tps900.uuid).toBeDefined();
      if (typeof window.tps900.uuid == 'string' || typeof window.tps900.uuid == 'object') {
        expect((String(window.tps900.uuid)).length > 0).toBe(true);
      } else {
        expect(window.tps900.uuid > 0).toBe(true);
      }
    });

    it("should contain a cordova specification that is a string", function() {
      expect(window.tps900.cordova).toBeDefined();
      expect((String(window.tps900.cordova)).length > 0).toBe(true);
    });

    it("should depend on the presence of cordova.version string", function() {
      expect(window.cordova.version).toBeDefined();
      expect((String(window.cordova.version)).length > 0).toBe(true);
    });

    it("should contain tps900.cordova equal to cordova.version", function() {
      expect(window.tps900.cordova).toBe(window.cordova.version);
    });

    it("should contain a model specification that is a string", function() {
      expect(window.tps900.model).toBeDefined();
      expect((String(window.tps900.model)).length > 0).toBe(true);
    });

    it("should contain a manufacturer property that is a string", function() {
      expect(window.tps900.manufacturer).toBeDefined();
      expect((String(window.tps900.manufacturer)).length > 0).toBe(true);
    });

    it("should contain an isVirtual property that is a boolean", function() {
      expect(window.tps900.isVirtual).toBeDefined();
      expect(typeof window.tps900.isVirtual).toBe("boolean");
    });

    it("should contain a serial number specification that is a string", function() {
      expect(window.tps900.serial).toBeDefined();
      expect((String(window.tps900.serial)).length > 0).toBe(true);

    });

  });
};

exports.defineManualTests = function(contentEl, createActionButton) {
    var logMessage = function (message, color) {
        var log = document.getElementById('info');
        var logLine = document.createElement('div');
        if (color) {
            logLine.style.color = color;
        }
        logLine.innerHTML = message;
        log.appendChild(logLine);
    };

    var clearLog = function () {
        var log = document.getElementById('info');
        log.innerHTML = '';
    };

    var tps900_tests = '<h3>Press Dump Tps900 button to get tps900 information</h3>' +
        '<div id="dump_tps900"></div>' +
        'Expected result: Status box will get updated with tps900 info. (i.e. platform, version, uuid, model, etc)';

    contentEl.innerHTML = '<div id="info"></div>' + tps900_tests;

    createActionButton('Dump tps900', function() {
      clearLog();
      logMessage(JSON.stringify(window.tps900, null, '\t'));
    }, "dump_tps900");
};

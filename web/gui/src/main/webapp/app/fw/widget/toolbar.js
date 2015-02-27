/*
 * Copyright 2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 ONOS GUI -- Widget -- Toolbar Service
 */
// TODO: Augment service to allow toolbars to exist on right edge of screen


(function () {
    'use strict';

    // injected refs
    var $log, fs, ps, bns, is;

    // configuration
    var arrowSize = 10,
        sepWidth = 6,
        defaultSettings = {
            edge: 'left',
            width: 20,
            margin: 0,
            hideMargin: -20,
            top: '80%',
            fade: false,
            shown: false
        };

    // internal state
    var tbars = {};


    // === Helper functions --------------------------------------

    // translate uses 50 because the svg viewbox is 50
    function rotateArrowLeft(adiv) {
        adiv.select('g')
            .attr('transform', 'translate(0 50) rotate(-90)');
    }
    function rotateArrowRight(adiv) {
        adiv.select('g')
            .attr('transform', 'translate(50 0) rotate(90)');
    }

    function createArrow(panel) {
        var arrowDiv = panel.append('div')
            .classed('tbarArrow', true)
            .style({
                'position': 'absolute',
                'top': '53%',
                'left': '96%',
                'margin-right': '-4%',
                'transform': 'translate(-50%, -50%)',
                'cursor': 'pointer'
            });
        is.loadIcon(arrowDiv, 'triangleUp', arrowSize, true);
        return arrowDiv;
    }

    function warn(msg, id) {
        $log.warn('createToolbar: ' + msg + ': [' + id + ']');
        return null;
    }


    // ==================================

    function createToolbar(id, opts) {
        if (!id) return warn('no ID given', id);
        if (tbars[id]) return warn('duplicate ID given', id);

        var settings = angular.extend({}, defaultSettings, fs.isO(opts)),
            tbid = 'toolbar-' + id,
            panel = ps.createPanel(tbid, settings),
            arrowDiv = createArrow(panel),
            tbWidth = arrowSize + 2;    // empty toolbar width


        arrowDiv.on('click', toggle);

        // add a descriptor for this toolbar
        tbars[id] = {
            settings: settings,
            panel: panel,
            panelId: tbid
        };

        panel.classed('toolbar', true)
            .style('top', settings.top);


        // API functions

        function addButton(id, gid, cb, tooltip) {
            var bid = tbid + '-' + id,
                btn = bns.button(panel, bid, gid, cb, tooltip);
            tbWidth += btn.width();
            panel.width(tbWidth);
            return btn;
        }

        function addToggle(id, gid, initState, cb, tooltip) {
            var tid = tbid + '-' + id,
                tog = bns.toggle(panel, tid, gid, initState, cb, tooltip);
            tbWidth += tog.width();
            panel.width(tbWidth);
            return tog;
        }

        function addRadioSet(id, rset) {
            var rid = tbid + '-' + id,
                rad = bns.radioSet(panel, rid, rset);
            tbWidth += rad.width();
            panel.width(tbWidth);
            return rad;
        }

        function addSeparator() {
            panel.append('div')
                .classed('separator', true);
            tbWidth += sepWidth;
        }

        function show(cb) {
            rotateArrowLeft(arrowDiv);
            panel.show(cb);
        }

        function hide(cb) {
            rotateArrowRight(arrowDiv);
            panel.hide(cb);
        }

        function toggle(cb) {
            if (panel.isVisible()) {
                hide(cb);
            } else {
                show(cb);
            }
        }

        return {
            addButton: addButton,
            addToggle: addToggle,
            addRadioSet: addRadioSet,
            addSeparator: addSeparator,

            show: show,
            hide: hide,
            toggle: toggle
        };
    }

    function destroyToolbar(id) {
        var tb = tbars[id];
        delete tbars[id];

        if (tb) {
            ps.destroyPanel(tb.panelId);
        }
    }

    // === Module Definition ===

    angular.module('onosWidget')
    .factory('ToolbarService',
        ['$log', 'FnService', 'PanelService', 'ButtonService', 'IconService',

        function (_$log_, _fs_, _ps_, _bns_, _is_) {
            $log = _$log_;
            fs = _fs_;
            ps = _ps_;
            bns = _bns_;
            is = _is_;

            // this function is only used in testing
            function init() {
                tbars = {};
            }

            return {
                init: init,
                createToolbar: createToolbar,
                destroyToolbar: destroyToolbar
            };
        }]);
}());

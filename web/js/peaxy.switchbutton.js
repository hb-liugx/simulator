PeaxySwitch = window.kendo.ui.Widget.extend({
        // initialization code goes here
    init: function(element, options) {
        // base call to initialize widget
        window.kendo.ui.Widget.fn.init.call(this, element, options);
        $(element).wrap('<div class="labelBox"></div>').parent().wrap('<div class="peaxy-switchbutton"></div>');
        $(element).after('<label class="check"></label>');
        $(element).parent().find(':last').click(function(e){
            $(element).trigger('click');
        });
    },
    options: {
        name: "PeaxySwitch"
    }
});
window.kendo.ui.plugin(PeaxySwitch);
var peaxy = peaxy || {};

peaxy.ProgressBar = kendo.Class.extend({
    progressBarContent:null,
    progressBarView:null,
    _options:{
        width: '200px',
        height: '5px',
        type:'',
        parent:document.body
    },
    init:function (options) {
        jQuery.extend(this._options,options);
        this.progressBarView = this._options.parent;
        this.progressBarContent = $('<div class="peaxy-progressbar">' + 
                                        '<div class="progressBar ' + this._options.type +'" style="width:'+ this._options.width +'; height:'+ this._options.height +'">' + 
                                            '<div name="overDiv" style="width:'+ this._options.width +'; height:100%;"></div>' + 
                                        '</div>' + 
                                    '</div>');
         this.progressBarView.append(this.progressBarContent);         
    },
    destroy: function(){
        this.progressBarView.remove();
    },
    setProgress:function(progress){
        if(progress <= 100)
            this.progressBarView.find('[name="overDiv"]').css("width", String(progress) + "%");
    }
});
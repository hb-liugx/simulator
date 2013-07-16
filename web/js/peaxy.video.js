var peaxy = peaxy || {};
peaxy.Video = kendo.Class.extend({
	init:function(content){
		this.content = content;
		var self = this;
		this.content.html(this.Template.instructionsHeader + this.Template.peaxyVideo);
		this.content = this.content.find("#videoContainer");
		//INITIALIZE
		var video = this.content.find('#myVideo');
		
		//remove default control when JS loaded
		video[0].removeAttribute("controls");
		$('.control').show();
	 
		//before everything get started
		video.on('loadedmetadata', function() {
			//set video properties
			self.content.find('.current').text(timeFormat(0));
			self.content.find('.duration').text(timeFormat(video[0].duration));
			updateVolume(0, 0.7);			
			//start to get video buffering data 
			setTimeout(startBuffer, 150);
		});
		
		//display video buffering bar
		var startBuffer = function() {
			var currentBuffer = video[0].buffered.end(0);
			var maxduration = video[0].duration;
			var perc = 100 * currentBuffer / maxduration;
			self.content.find('.bufferBar').css('width',perc+'%');			
			if(currentBuffer < maxduration) {
				setTimeout(startBuffer, 500);
			}
		};	
		
		//display current video play time
		video.on('timeupdate', function() {
			var currentPos = video[0].currentTime;
			var maxduration = video[0].duration;
			var perc = 100 * currentPos / maxduration;
			self.content.find('.timeBar').css('width',perc+'%');	
			self.content.find('.current').text(timeFormat(currentPos));	
		});
		
		//CONTROLS EVENTS
		//video screen and play button clicked
		video.on('click', function() { playpause();});
		self.content.find('.btnPlay').on('click', function() { playpause(); } );
		var playpause = function() {
			if(video[0].paused || video[0].ended) {
				self.content.find('.btnPlay').addClass('paused');
				video[0].play();
			}
			else {
				self.content.find('.btnPlay').removeClass('paused');
				video[0].pause();
			}
		};
		
		//stop button clicked
		this.content.find('.btnStop').on('click', function() {
			self.content.find('.btnPlay').removeClass('paused');
			updatebar(self.content.find('.progress').offset().left);
			video[0].pause();
		});

		$(document).bind('fullscreenchange  mozfullscreenchange webkitfullscreenchange',function(e){
			self.content.find('.btnFS').toggleClass('peaxy-i-video-restore');
		});

		//fullscreen button clicked
		self.content.find('.btnFS').on('click', function() {
			if(document.mozFullScreen || document.webkitIsFullScreen){
				if (document.exitFullscreen) {
				    document.exitFullscreen();  
			    }else if (document.mozCancelFullScreen) {  
			        document.mozCancelFullScreen();  
			    }else if (document.webkitCancelFullScreen) {  
			        document.webkitCancelFullScreen();  
			    }
			}else{
				if($.isFunction(video[0].webkitEnterFullscreen)){
					//self.content.find('#videoContainer').get(0).webkitRequestFullScreen();
					document.getElementById('myVideo').webkitRequestFullScreen();
				}else if ($.isFunction(video[0].mozRequestFullScreen)) {
					self.content.get(0).mozRequestFullScreen();
				}else {
					alert('Your browsers doesn\'t support fullscreen');
				}
			}
		});
		
		//sound button clicked
		self.content.find('.sound').click(function() {
			video[0].muted = !video[0].muted;
			$(this).toggleClass('muted');
			if(video[0].muted) {
				self.content.find('.volumeBar').css('width',0);
			}
			else{
				self.content.find('.volumeBar').css('width', video[0].volume*100+'%');
			}
		});
				
		//video canplaythrough event
		//solve Chrome cache issue
		var completeloaded = false;
		video.on('canplaythrough', function() {
			completeloaded = true;
		});
		
		//video ended event
		video.on('ended', function() {
			$('.btnPlay').removeClass('paused');
			video[0].pause();
		});

		//video seeked event
		video.on('seeked', function() { });
	
		//VIDEO PROGRESS BAR
		//when video timebar clicked
		var timeDrag = false;	/* check for drag event */
		this.content.find('.progress').on('mousedown', function(e) {
			timeDrag = true;
			updatebar(e.pageX);
		});
		$(document).on('mouseup', function(e) {
			if(timeDrag) {
				timeDrag = false;
				updatebar(e.pageX);
			}
		});
		$(document).on('mousemove', function(e) {
			if(timeDrag) {
				updatebar(e.pageX);
			}
		});
		var updatebar = function(x) {
			var progress = self.content.find('.progress');
			
			//calculate drag position
			//and update video currenttime
			//as well as progress bar
			var maxduration = video[0].duration;
			var position = x - progress.offset().left;
			var percentage = 100 * position / progress.width();
			if(percentage > 100) {
				percentage = 100;
			}
			if(percentage < 0) {
				percentage = 0;
			}
			self.content.find('.timeBar').css('width',percentage+'%');	
			video[0].currentTime = maxduration * percentage / 100;
		};

		//VOLUME BAR
		//volume bar event
		var volumeDrag = false;
		self.content.find('.volume').on('mousedown', function(e) {
			volumeDrag = true;
			video[0].muted = false;
			self.content.find('.sound').removeClass('muted');
			updateVolume(e.pageX);
		});
		$(document).on('mouseup', function(e) {
			if(volumeDrag) {
				volumeDrag = false;
				updateVolume(e.pageX);
			}
		});
		$(document).on('mousemove', function(e) {
			if(volumeDrag) {
				updateVolume(e.pageX);
			}
		});
		var updateVolume = function(x, vol) {
			var volume = self.content.find('.volume');
			var percentage;
			//if only volume have specificed
			//then direct update volume
			if(vol) {
				percentage = vol * 100;
			}
			else {
				var position = x - volume.offset().left;
				percentage = 100 * position / volume.width();
			}
			
			if(percentage > 100) {
				percentage = 100;
			}
			if(percentage < 0) {
				percentage = 0;
			}
			
			//update volume bar and video volume
			self.content.find('.volumeBar').css('width',percentage+'%');	
			video[0].volume = percentage / 100;
			
			//change sound icon based on volume
			if(video[0].volume == 0){
				self.content.find('.sound').removeClass('sound2').addClass('muted');
			}
			else if(video[0].volume > 0.5){
				self.content.find('.sound').removeClass('muted').addClass('sound2');
			}
			else{
				self.content.find('.sound').removeClass('muted').removeClass('sound2');
			}			
		};

		//Time format converter - 00:00
		var timeFormat = function(seconds){
			var m = Math.floor(seconds/60)<10 ? "0"+Math.floor(seconds/60) : Math.floor(seconds/60);
			var s = Math.floor(seconds-(m*60))<10 ? "0"+Math.floor(seconds-(m*60)) : Math.floor(seconds-(m*60));
			return m+":"+s;
		};
	},
	Template:{
		instructionsHeader:'<div class="p-content"><p class="p-title2">CREATING YOUR HYPERFILER</p>\
			<hr class="p-hr-l"/>\
			<p class="p-desc">In the next screens, we\'ll ask you about your storage management needs, and\
				based on your answers, create a Peaxy Hyperfiler that meets your needs. For a\
				primer, watch the video below.\
			</p></div>',
		peaxyVideo:'<div id="videoContainer" class="videoContainer" allowfullscreen>\
			<video id="myVideo" controls preload="auto" width="100%" height="100%" poster="img/peaxy_poster.png">\
			  <source src="-http://demo.inwebson.com/html5-video/iceage4.ogv" type="video/ogg" />\
			  <source src="-http://demo.inwebson.com/html5-video/iceage4.webm" type="video/webM" />\
			  <source src="-http://demo.inwebson.com/html5-video/iceage4.mp4" type="video/mp4" />\
			  <p>Your browser does not support the video tag.</p>\
			</video>\
			<div class="control">\
				<div class="topControl">\
					<div class="progress">\
						<span class="bufferBar"></span>\
						<span class="timeBar"></span>\
					</div>\
				</div>\
				<div class="btmControl">\
					<div class="peaxy-video-icon btnPlay btn " title="Play/Pause video"></div>\
					<div class="peaxy-video-icon sound sound2 btn" title="Mute/Unmute sound"></div>\
					<div class="volume" title="Set volume">\
						<span class="volumeBar"></span>\
					</div>\
					<div class="time">\
						<span class="current"></span> / <span class="duration"></span>\
					</div>\
					<div class="btn peaxy-video-icon  btnFS " title="Switch to full screen"></div>\
				</div>\
			</div>\
		</div>'
	}
});
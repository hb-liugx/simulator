var peaxy = peaxy || {};

peaxy.LicenseManager = kendo.Class.extend({
	init:function(options){
		this.setOptions(options);
	},
	setOptions:function(options){
		this._opt = {
			parent: $("body"),
			onChange:function(msg){},
			onSuccess:function(msg){}
		}	
		$.extend(this._opt,options);
	},
	// Load the import license page to the main window.
	loadImportLicensePage:function(handler){
		var self = this;
		this._opt.parent.html(this.Template.getImportLicensePage());

		var validator = peaxy.Validator,
			licenseFile = this._opt.parent.find('#licenseFile');
			licenseStatus = this._opt.parent.find('#licenseStatus');
		licenseFile.change(function(evt){
			var files = evt.target.files;
			var uploadedFile = files[0];
			var fileName = uploadedFile.name;
			var result = validator.checkFileType(uploadedFile, "zip");
			
			if(!result){
				self._opt.onChange(result);
				self._opt.parent.find("form").get(0).reset();
				licenseStatus.text("Only Zip file is allowed.");
			}else if(!validator.checkFileSize(uploadedFile, 3, "MB")){
				self._opt.onChange(result);
				self._opt.parent.find("form").get(0).reset();
				licenseStatus.text("The file size is not valid.");
			}else{
				self._opt.onChange(result);
				licenseStatus.text(fileName);
			}
      		
		});

		var submitButton = self._opt.parent.find('#submitButton');
	},
	importLicense:function(){
		var url = "/import/license";
		var reader = new FileReader();
		var license = this._opt.parent.find(":file").get(0).files[0];
		var fd = new FormData();
		fd.append('licensefile', license);
		peaxy.Communication.submitForm(url,fd,{onSuccess:function(data, satus){
			if(status==200){
				self._opt.onSuccess(data);
				self.loadReviewLicensePage(data);
			}else{
				alert("Uploading Error, please check again.");
			}
		}});
	},
	loadReviewLicensePage:function(handler){
		var self = this;
		this._opt.parent.html(this.Template.getLicenseReviewPage());

		var check  = new peaxy.Check(),
			backToImportLink = this._opt.parent.find('#backToImport');
			licenseName = this._opt.parent.find('#licenseName');
			licenseType =this._opt.parent.find('#licenseType');
			spaceCapacity = this._opt.parent.find('#spaceCapacity');
			supportLevel = this._opt.parent.find('#supportLevel');
			additionalFeatures = this._opt.parent.find('#additionalFeatures');

		// licenseName.html(hander[licenseName]);	
		// licenseType.html(hander[licenseType]);	
		// spaceCapacity.html(hander[spaceCapacity]);	
		// supportLevel.html(hander[licenseType]);	
		// additionalFeatures.html(hander[additionalFeatures]);
		
		backToImportLink.click(function(){
			self.loadImportLicensePage();
		});
	},

	Template:(function(){
		var importLicesePage = '<div class="p-content">\
		<div class="licenseImportContent">\
			<div class="p-title1">IMPORT HYPERFILER LICENSE</div>\
			<div class="p-hr-l"></div>\
			<div class="p-desc">Import the Hyperfiler License that you received to activate and create a Peaxy Hyperfiler. If you have lost your Peaxy Hyperfiler License, please contact <a href="mailto:licensesupport@peaxy.net">licensesupport@peaxy.net</a>.</div>\
			<div class="importLicenseDiv">\
				<form enctype="multipart/form-data">\
					<button type="button" id="fakeButton" class="s-button">SELECT HYPERFILER LICENSE</button>\
					<input type="file" id="licenseFile" class="licenseUpload" accept="application/x-zip-compressed"/>\
					<div id="licenseStatus" class="licenseStatus"></div>\
				</form>\
			</div>\
		</div>\
		</div>';
		var licenseReviewPage = '<div  class="p-content">\
			<div class="p-title1">REVIEW HYPERFILER LICENSE</div>\
			<div class="p-hr-l"></div>\
			<div class="p-desc">Please review and validate that these license characteristics are correct. <br/>Wrong License? <a href="#" id="backToImport">Replace with another Hyperfiler License</a>.\
				<br/>If you notice issues with your license, please contact Peaxy License Support at <a href="mailto:licensesupport@peaxy.net">licensesupport@peaxy.net</a>.\
			</div>\
			<div class="licenseInfo">\
				<table class="reviewLicenseTable">\
					<tr>\
						<td width="490px">\
							<div id="licenseName"></div>\
						</td>\
						<td>\
						</td>\
					</tr>\
					<tr>\
						<td>\
							<div class="p-title3">LICENSE TYPE</div>\
							<div id="licenseType" class="p-desc"></div>\
						</td>\
						<td>\
							<div class="help" data-help-url="license/licensereviewinfo/licensetype"></div>\
						</td>\
					</tr>\
					<tr>\
						<td>\
							<div class="p-title3">DATA SPACE CAPACITY LEVEL</div>\
							<div id="spaceCapacity" class="p-desc"></div>\
						</td>\
						<td>\
							<div class="help" data-help-url="license/licensereviewinfo/dataspace"></div>\
						</td>\
					</tr>\
					<tr>\
						<td>\
							<div class="p-title3">SUPPORT LEVEL</div>\
							<div id="supportLevel" class="p-desc"></div>\
						</td>\
						<td>\
							<div class="help" data-help-url="license/licensereviewinfo/supportlevel"></div>\
						</td>\
					</tr>\
					<tr>\
						<td>\
							<div class="p-title3">ADDITIONAL FEATURES</div>\
							<div id="additionalFeatures" class="p-desc"></div>\
						</td>\
						<td>\
							<div class="help" data-help-url="license/licensereviewinfo/additionalfeatures"></div>\
						</td>\
					</tr>\
				</table>\
			</div>\
		</div>';
		return {
			getImportLicensePage:function(){return importLicesePage;},
			getLicenseReviewPage:function(){return licenseReviewPage;}
		}
	}()),	
});

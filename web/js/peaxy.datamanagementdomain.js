var peaxy = peaxy || {};
peaxy.ManagementDomain = {
	MigrationRequestViewModel : function(){
		
	},
	PoliciesResponseViewModel : function(){
		
	},
	CardViewModel : function(){
		this.performanceLevel;	//performance : 1-16
		this.className;			//className
		this.replicas;			//replicas : 1-4
		this.usableSpace;		//number
		this.freeSpace;			//number
		this.totalSpace;		//number
		this.usedSpace;			//number
		this.flexible;			//flexible : YES/NO
		this.defaultValue;		//statesName: DEFAULT/''
	},
	MigrationModel : function(){
		this.id;
		this.name;
		this.description;
		this.condition = function(){
			this.dir;
			this.extension;
			this.unModifiedMinutes;
			this.unAccessedMinutes;
			this.currentSC;
		};
		this.destination;
		this.schedule = function(){
			this.start;
			this.interval;
		};
	},
	IngestModel : function(){
		this.id;
		this.name;
		this.groupId;
		this.parentPath;
		this.fileType;
		this.description;
		this.storageClassId;
		this.storageClassName;
		this.flag;
	},
	IngestViewModel : function(){
		this.id;
		this.name;
		this.condition = [function(){
			this.name;
			this.vals;
		}];
		this.ands = [function(){
			this.key;
			this.vals = [];
		}];
		this.others = [function(){
			this.key;
			this.vals;
			this.type;
		}];
		this.description;
		this.schedule = function(){
			this.start;
			this.time;
			this.execute;
			this.date;
			this.zone;
			this.repeat;
		};
	}
}

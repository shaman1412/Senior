
'use strict';

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const images = require('../lib/images');
const delete_from_gcloud = require('../lib/delete_image_from_gcloud');

const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

router.use((req, res, next) => {
  res.set('Content-Type', 'text/html');
  next();
});

function getModel () {
  return require(`./promotion_method`);
}


router.get('/', (req, res, next) => {
  getModel().list(10, req.query.pageToken, (err, entities, cursor) => {
    if (err) {
      next(err);
      return;
    }
    res.json({
      items: entities,
      nextPageToken: cursor
    });
  });
});

router.get('/get_count', (req, res, next) => {
  getModel().get_count((err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.get('/:promotionid', (req, res, next) => {
  getModel().read(req.params.promotionid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.post('/new_promotion',images.multer.array('image'),images.sendUploadToGCS,(req, res, next) => {
	const json  = req.body;
	if (req.file && req.file.cloudStoragePublicUrl) {
		json.promotionpictureurl = req.file.cloudStoragePublicUrl;
    }
	else if(req.files)
	{
		var ls=req.files;
		json.promotionpictureurl = "";
		var n = ls.length;
		var i = 0;
		ls.forEach(function(item){
			if(!(++i == n))
			{
				json.promotionpictureurl = json.promotionpictureurl + item.cloudStoragePublicUrl+",";
			}
			else
			{
				json.promotionpictureurl = json.promotionpictureurl + item.cloudStoragePublicUrl;
			}
		});
	}
	const promotion = {
		promotionname: json.promotionname,
		promotionpictureurl: json.promotionpictureurl,
		promotionstartdate: json.promotionstartdate,
		promotionenddate: json.promotionenddate,
		promotiondetail: json.promotiondetail,
		promotionlocation: json.promotionlocation,
		promotionuserid : json.promotionuserid
	}
	console.log("\n\n");
	console.log(promotion.promotionpictureurl);
	
	getModel().create(promotion,(err,entities) => {
		if(err){
			return next(err);
		}
		//res.json(entities);
	});	
	
	return res.send(promotion.promotionpictureurl);
});

//update
router.put('/:promotionid', images.multer.array('image'), (req, res, next) => {
	
	const json  = req.body;
	console.log(json);
	
	const old_filename = json.old_filename;
	delete_from_gcloud.deleteImageFromPromotion (old_filename, res, next);
	
	images.sendUploadToGCS (req, res, next);
	if (req.file && req.file.cloudStoragePublicUrl) {
		json.picture = req.file.cloudStoragePublicUrl;
    }
	else if(req.files)
	{
		var ls=req.files;
		json.picture = "";
		var n = ls.length;
		var i = 0;
		ls.forEach(function(item){
			if(!(++i == n))
			{
				json.picture = json.picture + item.cloudStoragePublicUrl+",";
			}
			else
			{
				json.picture = json.picture + item.cloudStoragePublicUrl;
			}
		});
	}
	
	const promotion = {		
		promotionname: json.promotionname,
		promotionpictureurl: json.promotionpictureurl,
		promotionstartdate: json.promotionstartdate,
		promotionenddate: json.promotionenddate,
		promotiondetail: json.promotiondetail,
		promotionlocation: json.promotionlocation
	}	
	
	getModel().update(req.params.promotionid, promotion, (err, entity) => {
		if (err) {
			next(err);
			return;
		}
		res.json(entity);
	});
});

router.use((err, req, res, next) => {
  // Format error and forward to generic error handler for logging and
  // responding to the request
  err.response = {
    message: err.message,
    internalCode: err.code
  };
  next(err);
});

module.exports = router;

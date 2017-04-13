
'use strict';

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const images = require('../lib/images');

const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

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
		data.imageUrl = "";
		var n = ls.length;
		var i = 0;
		ls.forEach(function(item){
			if(!(++i == n))
			{
				data.imageUrl = data.imageUrl + item.cloudStoragePublicUrl+",";
			}
			else
			{
				data.imageUrl = data.imageUrl + item.cloudStoragePublicUrl;
			}
		});
	}
	const promotion = {
		promotionname: json.promotionname,
		promotionpictureurl: json.promotionpictureurl,
		promotiontype: json.promotiontype,
		promotionstartdate: json.promotionstartdate,
		promotionenddate: json.promotionenddate,
		promotiondetail: json.promotiondetail,
		promotionlocation: json.promotionlocation
	}
	
	if (req.file && req.file.cloudStoragePublicUrl) {
      json.promotionpictureurl = req.file.cloudStoragePublicUrl;
    }
	
	getModel().create(promotion,(err,entities) => {
		if(err){
			next(err);
			return;
		}
		res.json(entities)
	})
});

router.put('/:promotionid', (req, res, next) => {
	const json  = req.body;
	console.log(json);
	const promotion = {		
		promotionname: json.promotionname,
		promotionpictureurl: json.promotionpictureurl,
		promotiontype: json.promotiontype,
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

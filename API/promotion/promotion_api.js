
'use strict';

const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');

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


router.post('/new_promotion',(req, res, next) => {
	const json  = req.body;
	console.log(json);
	const promotion = {
		promotionname: json.promotionname,
		promotionstartdate: json.promotionstartdate,
		promotionenddate: json.promotionenddate,
		promotiondetail: json.promotiondetail,
		promotionlocation: json.promotionlocation,
		promotiontype:json.promotiontype
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
		promotionstartdate: json.promotionstartdate,
		promotionenddate: json.promotionenddate,
		promotiondetail: json.promotiondetail,
		promotionlocation: json.promotionlocation,
		promotiontype:json.promotiontype
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

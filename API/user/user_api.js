
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
  return require(`./user_method`);
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

router.get('/:userid', (req, res, next) => {
  getModel().read(req.params.userid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});


router.post('/new_user',(req, res, next) => {
	const json  = req.body;
	console.log(json);
	const user = {
		userid : json.userid,
		name: json.name,
    picture : json.picture,
		address: json.address,
		email: json.email,
		telephone: json.telephone,
		gender: json.gender,
    favourite_type : json.favourite_type,
		age: json.age
	}
	getModel().create(user,(err,entities) => {
		if(err){
			next(err);
			return;
		}
		res.json(entities)
	})


})

router.put('/:userid', (req, res, next) => {
	const json  = req.body;
	console.log(json);
	const user = {		
		name: json.name,
		address: json.address,
		telephone: json.telephone,

    favourite_type : json.favourite_type,
		
	}	
  getModel().update(req.params.userid, user, (err, entity) => {
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

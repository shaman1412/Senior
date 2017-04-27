
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
  return require(`./login_method`);
}

router.put('/change_pass/:userid', (req, res, next) => {
  const json = req.body;
  const data = {
    password: json.password
  }
  getModel().update(req.params.userid, data, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.get('/:username', (req, res, next) => {
  getModel().read(req.params.username, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});


router.get('/userid/:userid', (req, res, next) => {
  getModel().read_userid(req.params.userid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.post('/register',(req, res, next) => {
	const json = req.body;
	//const token = tokenGenerator(json.username);
	const data = {
		username:json.username,
		password:json.password,
		userid:json.userid
	}
	getModel().create(data,(err,entities) => {
		if(err){
			next(err);
			return;
		}
		res.json(entities)
	})
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

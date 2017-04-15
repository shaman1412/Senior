'use strict'
const express = require('express');
const bodyParser = require('body-parser');
const config = require('../config');
const router = express.Router();

router.use(bodyParser.json());
router.use(bodyParser.urlencoded({
    extended: true
}));

function getModel () {
  return require('./party_method');
}

router.get('/userid/:group_userid' , (req, res, next )=>{
	getModel().getdetail_userid(req.params.group_userid , (err,entity) =>{
		if(err){
			next(err);
			return;
		}
		res.json(entity);
	})
})

module.exports = router; 

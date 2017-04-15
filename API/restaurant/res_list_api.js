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
  return require('./res_list_method');
}

router.get('/nearby/:lat/:long', (req, res, next) => {

  getModel().nearby_list(req.params.lat,req.params.long, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});

router.get('/top',(req, res , next) =>{
	getModel().top_list((err, entity) => {
		if(err){
			next(err);
			return;
		}
		res.json(entity);
	});
});

router.get('/recommend/:type_food', (req,res,next) =>{
	 const type_food = req.params.type_food;

	getModel().recommend_list( type_food, (err, entity) => {
		if(err){
			next(err);
			return;
		}
		res.json(entity);
	});
});

router.get('/all' , (req, res, next )=>{
	getModel().all_restaurant((err,entity) =>{
		if(err){
			next(err);
			return;
		}
		res.json(entity);
	})
})





module.exports = router; 
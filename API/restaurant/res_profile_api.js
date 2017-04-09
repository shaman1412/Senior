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
  return require('./res_profile_method');
}

router.get('/:resid', (req, res, next) => {
  getModel().read(req.params.resid, (err, entity) => {
    if (err) {
      next(err);
      return;
    }
    res.json(entity);
  });
});
router.post('/create', (req,res,next) => {
  const json = req.body;
  const data  =  {
    res_id : json.res_id,
    name : json.name,
    type_food :  json.type_food,
    description : json.description,
    period : json.period,
    address : json.address,
    location :  json.location,
    score : 0,
    vote : 0,
    create_time : json.create_time

  }
  getModel().create(data, (err, entity) => {
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  })
})

router.put('/upscore/:resid', (req, res, next) => {
  const json = req.body;
    getModel().calculate_score(req.params.resid, json.score, (err, entity) => {
      if(err){
        next(err);
        return;
      }
      res.json(entity);
    })
})
router.put('/update/:resid',(req,res,next)=>{
  const json = req.body;
  const data = {
    name : json.name,
    type_food :  json.type_food,
    description : json.description,
    period : json.period,
    address : json.address,
    location :  json.location
  }
	getModel().update(req.params.resid, data ,  (err,entity) =>{
		if (err) {
			next(err);
			return;
		}
		res.json(entity);
	})

})
router.delete('/del/:resid', (req, res, next) => {
  getModel().delete(req.params.resid , (err, entity) =>{
    if(err){
      next(err);
      return;
    }
    res.json(entity);
  })
})

module.exports = router; 
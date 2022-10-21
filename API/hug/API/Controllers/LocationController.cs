using Microsoft.AspNetCore.Mvc;
using Models;
using Repository.Interfaces;
using static Dapper.SqlMapper;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LocationController : ControllerBase
    {
        private readonly IUnitOfWork repository;

        public LocationController(IUnitOfWork _repository)
        {
            this.repository = _repository;
        }

        // GET: api/<LocationController>
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            try
            {
                return Ok(repository.Location.GetAllAsync());
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // GET api/<LocationController>/5
        [HttpGet("{id}")]
        public async Task<IActionResult> Get(int id)
        {
            try
            {
                return Ok(repository.Location.GetByIdAsync(id));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // POST api/<LocationController>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Location entity)
        {
            try
            {
                entity.CreatedDate = DateTime.UtcNow;
                return Ok(repository.Location.AddAsync(entity));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // PUT api/<LocationController>/5
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] Location entity)
        {
            try
            {
                entity.ModifiedDate = DateTime.UtcNow;
                return Ok(repository.Location.UpdateAsync(entity));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }

        // DELETE api/<LocationController>/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                return Ok(repository.Location.DeleteAsync(id));
            }
            catch (Exception ex)
            {
                return StatusCode(500, "Server Error.");
            }
        }
    }
}

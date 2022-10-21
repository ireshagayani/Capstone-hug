using API.Helper;
using Microsoft.AspNetCore.Mvc;
using Models;
using Repository.Interfaces;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {

        private readonly IUnitOfWork unitOfWork;

        public AuthController(IUnitOfWork _unitOfWork)
        {
            this.unitOfWork = _unitOfWork;
        }

        // POST api/<AuthController>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Auth entity)
        {
            try
            {
                return Ok(await unitOfWork.User.Login(entity.Username, Cryptography.Encrypt(entity.Password)));
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }
    }
}
